package fi.hiit.meerkat.protocol;

import fi.hiit.meerkat.MeerkatApplication;

/**
  */
public class StartCommand implements ICommand
{
    public void execute(MeerkatApplication application)
    {
        //[TODO]
        application.start();
    }
}

