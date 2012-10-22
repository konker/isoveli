package fi.hiit.meerkat.protocol;

import fi.hiit.meerkat.MeerkatApplication;

/**
  */
public interface ICommand
{
    void execute(MeerkatApplication application);
}
