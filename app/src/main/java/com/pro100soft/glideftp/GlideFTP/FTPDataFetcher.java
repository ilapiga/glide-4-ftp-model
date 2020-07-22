package com.pro100soft.glideftp.GlideFTP;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.InputStream;


public class FTPDataFetcher implements DataFetcher<InputStream> {
    private final FTPModel model;
    private InputStream stream;
    FTPClient ftpClient;

    FTPDataFetcher(FTPModel model) {
        this.model = model;
        ftpClient = new FTPClient();
        // Saves the character encoding to be used by the FTP control connection.
        // Some FTP servers require that commands be issued in a non-ASCII encoding
        // like UTF-8 so that filenames with multi-byte character representations (e.g, Big 8) can be specified.
        // ftpClient.setControlEncoding("UTF-8");
        ftpClient.setConnectTimeout(5 * 1000); // 5s
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            //if(!Com.isNetworkAvailable(App.getContext()))
            //    throw new Exception(App.getContext().getString(R.string.no_network_connection));

            int reply; // Server response value
            if (model.getPort() == null) {
                ftpClient.connect(model.getServer());
            } else {
                ftpClient.connect(model.getServer(), Integer.parseInt(model.getPort()));
            }
            // Get response value
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                // Disconnect
                ftpClient.disconnect();
                throw new Exception("Can't connect to " + model.getServer()
                        + ":" + model.getPort()
                        + ". The server response is: " + ftpClient.getReplyString());
            }

            ftpClient.login(model.getUsername(), model.getPassword());
            // Get response value
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                // Disconnect
                ftpClient.disconnect();
                throw new Exception("Error while performing login on " + model.getServer()
                        + ":" + model.getPort()
                        + " with username: " + model.getUsername()
                        + ". Check your credentials and try again.");

            }

            // Get login information
            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("en");//"uk"
            ftpClient.configure(config);
            ftpClient.enterLocalPassiveMode();// Use passive mode as default
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// Binary file support

            // First determine whether the server file exists
            FTPFile[] files = ftpClient.listFiles(model.getFilePath());
            if (files.length == 0 || !files[0].isFile())
                throw new Exception("File " + model.getFilePath() + " does not exist");

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            stream = ftpClient.retrieveFileStream(model.getFilePath());
            callback.onDataReady(stream);

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            callback.onLoadFailed(ex);
        }
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ftpClient = null;
    }

//    @Override
//    public String getId() {
//        return model.ftpPath;
//    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

}
