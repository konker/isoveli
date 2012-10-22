package fi.hiit.meerkat.protocol;

import java.util.List;
import java.util.ListArray;

import fi.hiit.meerkat.MeerkatApplication;

/**
  */
// [TODO:  does this need a base class/interface?]
public class UsbProtocol
{
    private MeerkatApplication mApplication;
    //private List<ICommand> mHistory();

    /**
      Create a command object from the given raw bytes.
      Currently this is very simple. START|STOP.
      Possible future extensions will allow the protocol
      to configure the data sources, so will have a more
      structured format, e.g. JSON.
    */
    public static ICommand createCommand(byte[] raw)
    {
        String s = new String(raw);
        switch (s) {
            case "START":
                return new StartCommand();
            case "STOP":
                return new StopCommand();
        }
        return null;
    }

    public UsbProtocol(mApplication application)
    {
        //[FIXME: do we need to store history?]
        //mHistory = new ArrayList<ICommand>();
    }

    public void storeAndExecute(byte[] raw)
    {
        storeAndExecute(createCommand(raw));
    }

    public void storeAndExecute(ICommand command)
    {
        //mHistory.add(command);
        if (command != null) {
            command.execute(mApplication);
        }
    }
}

