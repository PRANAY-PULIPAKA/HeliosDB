package com.heliosdb.command;

import com.heliosdb.service.KeyValueService;

public class CommandRegistry {

    private final KeyValueService service;

    public CommandRegistry(KeyValueService service) {
        this.service = service;
    }

    public void setReplayMode(boolean mode) {
        service.setReplayMode(mode);
    }

    public KeyValueService getService() {
        return service;
    }
}