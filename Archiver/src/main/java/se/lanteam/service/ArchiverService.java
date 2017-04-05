package se.lanteam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.repository.StoredProcRepository;

/**
 * Created by Björn Törnqvist, ArctiSys AB, 2016-02
 */
@Service
public class ArchiverService {

	private static final Logger LOG = LoggerFactory.getLogger(ArchiverService.class);

    private StoredProcRepository spRepo;
    
	public void archiveOrders() {
		LOG.debug("before archive");
		String result = spRepo.doArchiving();
		LOG.debug("after archive: " + result);
	}    
		
	@Autowired
	public void setOrderRepo(StoredProcRepository spRepo) {
		this.spRepo = spRepo;
	}
}
