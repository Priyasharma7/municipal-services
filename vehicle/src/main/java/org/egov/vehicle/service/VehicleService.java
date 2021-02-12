package org.egov.vehicle.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.vehicle.repository.VehicleRepository;
import org.egov.vehicle.util.VehicleErrorConstants;
import org.egov.vehicle.util.VehicleUtil;
import org.egov.vehicle.validator.Validator;
import org.egov.vehicle.web.model.Vehicle;
import org.egov.vehicle.web.model.VehicleRequest;
import org.egov.vehicle.web.model.VehicleSearchCriteria;
import org.egov.vehicle.web.model.user.User;
import org.egov.vehicle.web.model.user.UserDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class VehicleService {

    @Autowired
    private VehicleUtil util;

    @Autowired
    private EnrichmentService enrichmentService;

    @Autowired
    private VehicleRepository repository;
    
    @Autowired
    private Validator validator;
    
    @Autowired
    private UserService userService;

    public Vehicle create(VehicleRequest vehicleRequest) {
    		
    	RequestInfo requestInfo = vehicleRequest.getRequestInfo();
		String tenantId = vehicleRequest.getVehicle().getTenantId().split("\\.")[0];
		Object mdmsData = util.mDMSCall(requestInfo, tenantId);
		if (vehicleRequest.getVehicle().getTenantId().split("\\.").length == 1) {
			throw new CustomException(VehicleErrorConstants.INVALID_TENANT, " Application cannot be create at StateLevel");
		}
		validator.validateCreate(vehicleRequest,mdmsData);
        enrichmentService.enrichVehicleCreateRequest(vehicleRequest);
        repository.save(vehicleRequest);
        return vehicleRequest.getVehicle();
    }

	public List<Vehicle> search(@Valid VehicleSearchCriteria criteria, RequestInfo requestInfo) {
		validator.validateSearch(requestInfo, criteria);
		UserDetailResponse usersRespnse;
		List<String> uuids = new ArrayList<String>();
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		
		if(criteria.tenantIdOnly() ) {
			throw new CustomException(VehicleErrorConstants.INVALID_SEARCH, " Atlest one parameter is mandatory!");
		}
		
		if( criteria.getMobileNumber() !=null) {
			usersRespnse = userService.getOwner(criteria,requestInfo);
			if(usersRespnse !=null && usersRespnse.getUser() != null && usersRespnse.getUser().size() >0) {
				uuids = usersRespnse.getUser().stream().map(User::getUuid).collect(Collectors.toList());
				criteria.setMobileIds(uuids);
			}
		}
		
		vehicleList = repository.getVehicleData(criteria);
		
		if(!vehicleList.isEmpty()) {
			enrichmentService.enrichSearchData(vehicleList,requestInfo);
		}
		return vehicleList;
	}

}