package fi.hiit.meerkat.datasource;

import java.util.List;    
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.Date;

import android.util.Log;
import android.content.Context;
import android.content.IntentFilter;    
import android.content.Intent;     

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.Command;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;

/**
 */
public class TcpdumpDataSource extends AbstractPeriodicDataSource
{
    private String mResults;

    public TcpdumpDataSource(IDataSink sink, byte channelId, int periodMs, int durationMs)
    {
        super(sink, channelId, periodMs, durationMs);
        Log.d(MeerkatApplication.TAG, "TcpdumpDataSource.ctor");
    }

    @Override
    public String getLabel()
    {
        return "Tcpdump network sniffer Data Source";
    }

    @Override
    public String getDescription()
    {
        return "Tcpdump network sniffer data source. Periodically sniff network packets.";
    }

    @Override
    public void start()
    {
        Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.start");
        if (init()) {
            super.start();
        }
    }

    @Override
    public void stop()
    {
        Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.stop");
        super.stop();
    }

    public boolean init()
    {
        Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.init");
        if (!RootTools.isRootAvailable()) {
            // [FIXME: how should this be handled?]
            Log.i(MeerkatApplication.TAG,  "No root access... stopping.");
            stop();
            return false;
        }
        return true;
    }

    @Override
    public void tick()
    {
        Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.tick");

        String COMMAND = "/data/data/fi.hiit.meerkat/tcpdump -i wlan0 -c 100 tcp"; 
        try {
            Command command = new Command(0, COMMAND) {
                @Override
                public void output(int id, String line) {
                    mResults = line;
                }
            };
            long st = (new Date()).getTime();
            Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.tick: before exec: " + st);
            RootTools.getShell(true).add(command).waitForFinish(mDurationMs);
            long et = (new Date()).getTime();
            Log.d(MeerkatApplication.TAG,  "TcpdumpDataSource.tick: after exec: " + et + ": " + (st - et));

            /*
            // Capture 100 tcp packets on interface wlan0
            Process process = Runtime.getRuntime().exec("/data/data/fi.hiit.meerkat/tcpdump -i wlan0 -q -c 100 tcp");
            
            // Reads stdout.
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer mResults = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                mResults.append(buffer, 0, read);
            }
            reader.close();
            
            // Waits for the command to finish.
            process.waitFor();
            */
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        // write JSON to data sink
        try {
            mSink.write(mChannelId, mResults.toString());
        }
        catch (DataSinkPacketTooBigException ex) {
            Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
        }
    }
}


