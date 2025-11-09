package com.docencia.dam.services.interfaces;

import com.docencia.dam.domain.Job;

public interface CommandService {
    String execute(Job job);

    String name();
}
