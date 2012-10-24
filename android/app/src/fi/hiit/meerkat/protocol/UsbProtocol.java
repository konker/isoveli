package fi.hiit.meerkat.protocol;

import java.util.List;
import java.util.ArrayList;

import fi.hiit.meerkat.MeerkatApplication;

/**
  */
// [TODO:  does this need a base class/interface?]
public class UsbProtocol
{
    //private List<ICommand> mHistory();

    /**
      Create a command object from the given raw bytes.
      Currently this is very simple. START|STOP.
      Possible future extensions will allow the protocol
      to configure the data sources, so will have a more
      structured format, e.g. JSON.
    */
    public static ICommand createCommand(String s)
    {
        if (s.equals("START")) {
            return new StartCommand();
        }
        else if (s.equals("STOP")) {
            return new StopCommand();
        }
        return null;
    }

    public UsbProtocol()
    {
        //[FIXME: do we need to store history?]
        //mHistory = new ArrayList<ICommand>();
    }

    public void storeAndExecute(String s)
    {
        storeAndExecute(createCommand(s));
    }

    public void storeAndExecute(ICommand command)
    {
        //mHistory.add(command);
        if (command != null) {
            command.execute();
        }
    }
}

