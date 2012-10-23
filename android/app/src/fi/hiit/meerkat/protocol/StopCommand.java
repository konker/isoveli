package fi.hiit.meerkat.protocol;

import fi.hiit.meerkat.MeerkatApplication;

/**
  */
public class StopCommand implements ICommand
{
    public void execute()
    {
        MeerkatApplication.getInstance().stop();
    }
}

