package org.egov.vehiclelog.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.fsm.util.ResponseInfoFactory;
import org.egov.fsm.web.model.FSM;
import org.egov.fsm.web.model.FSMRequest;
import org.egov.fsm.web.model.FSMResponse;
import org.egov.vehiclelog.service.VehicleLogService;
import org.egov.vehiclelog.util.VehicleLogUtil;
import org.egov.vehiclelog.web.model.VehicleLog;
import org.egov.vehiclelog.web.model.VehicleLogRequest;
import org.egov.vehiclelog.web.model.VehicleLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vehicleLog")
public class VehicleLgController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private VehicleLogUtil vehicleLogUtil;
	
	@Autowired
	private VehicleLogService vehicleLogService;
	
	@PostMapping(value = "/_create")
	public ResponseEntity<VehicleLogResponse> create(@Valid @RequestBody VehicleLogRequest request) {
		
		vehicleLogUtil.defaultJsonPathConfig();
		VehicleLog vehicleLog = vehicleLogService.create(request);
		List<VehicleLog> vehicleLogList = new ArrayList<VehicleLog>();
		vehicleLogList.add(vehicleLog);
		VehicleLogResponse response = VehicleLogResponse.builder().vehicleLog(vehicleLogList)				
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
