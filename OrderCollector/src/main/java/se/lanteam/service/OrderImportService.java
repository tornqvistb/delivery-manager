package se.lanteam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import se.lanteam.io.FileManager;

/**
 * Created by david on 2015-01-20.
 */
@Service
public class OrderImportService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderImportService.class);

    public void hello() {
        LOG.info("Hello World!");
        FileManager fileMan = new FileManager();
        fileMan.moveFiles();
    }
}
