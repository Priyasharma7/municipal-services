package org.egov.waterConnection.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.waterConnection.constants.WCConstants;
import org.egov.waterConnection.model.Connection.ApplicationStatusEnum;
import org.egov.waterConnection.model.Connection.StatusEnum;
import org.egov.waterConnection.model.Document;
import org.egov.waterConnection.model.PlumberInfo;
import org.egov.waterConnection.model.Property;
import org.egov.waterConnection.model.WaterConnection;
import org.egov.waterConnection.model.enums.Status;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class WaterRowMapper implements ResultSetExtractor<List<WaterConnection>> {

	@Override
	public List<WaterConnection> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, WaterConnection> connectionListMap = new HashMap<>();
		WaterConnection currentWaterConnection = new WaterConnection();
		while (rs.next()) {
			String Id = rs.getString("connection_Id");
			if (connectionListMap.getOrDefault(Id, null) == null) {
				currentWaterConnection = new WaterConnection();
				currentWaterConnection.setConnectionCategory(rs.getString("connectionCategory"));
				currentWaterConnection.setRainWaterHarvesting(rs.getBoolean("rainWaterHarvesting"));
				currentWaterConnection.setConnectionType(rs.getString("connectionType"));
				currentWaterConnection.setWaterSource(rs.getString("waterSource"));
				currentWaterConnection.setMeterId(rs.getString("meterId"));
				currentWaterConnection.setMeterInstallationDate(rs.getLong("meterInstallationDate"));
				currentWaterConnection.setId(rs.getString("connection_Id"));
				currentWaterConnection.setApplicationNo(rs.getString("applicationNo"));
				currentWaterConnection
						.setApplicationStatus(ApplicationStatusEnum.fromValue(rs.getString("applicationstatus")));
				currentWaterConnection.setStatus(StatusEnum.fromValue(rs.getString("status")));
				currentWaterConnection.setConnectionNo(rs.getString("connectionNo"));
				currentWaterConnection.setOldConnectionNo(rs.getString("oldConnectionNo"));
				currentWaterConnection.setPipeSize(rs.getDouble("pipeSize"));
				currentWaterConnection.setNoOfTaps(rs.getInt("noOfTaps"));
				currentWaterConnection.setProposedPipeSize(rs.getDouble("proposedPipeSize"));
				currentWaterConnection.setProposedTaps(rs.getInt("proposedTaps"));
				currentWaterConnection.setWaterSubSource(rs.getString("waterSubSource"));
				currentWaterConnection.setAction(rs.getString("action"));
				currentWaterConnection.setRoadCuttingArea(rs.getFloat("roadcuttingarea"));
				currentWaterConnection.setRoadType(rs.getString("roadtype"));
				// get property id and get property object
				Property property = new Property();
				property.setPropertyId(rs.getString("property_id"));
				HashMap<String, Object> penalties = new HashMap<>();
				penalties.put(WCConstants.ADHOC_PENALTY, rs.getBigDecimal("adhocpenalty"));
				penalties.put(WCConstants.ADHOC_REBATE, rs.getBigDecimal("adhocrebate"));
				penalties.put(WCConstants.ADHOC_PENALTY_REASON, rs.getString("adhocpenaltyreason"));
				penalties.put(WCConstants.ADHOC_PENALTY_COMMENT, rs.getString("adhocpenaltycomment"));
				penalties.put(WCConstants.ADHOC_REBATE_REASON, rs.getString("adhocrebatereason"));
				penalties.put(WCConstants.ADHOC_REBATE_COMMENT, rs.getString("adhocrebatecomment"));
				currentWaterConnection.setAdditionalDetails(penalties);
				currentWaterConnection.setProperty(property);
				// Add documents id's
				currentWaterConnection.setConnectionExecutionDate(rs.getLong("connectionExecutionDate"));
				connectionListMap.put(Id, currentWaterConnection);
			}
			addChildrenToProperty(rs, currentWaterConnection);
		}
		return new ArrayList<>(connectionListMap.values());
	}

	private void addChildrenToProperty(ResultSet rs, WaterConnection waterConnection) throws SQLException {
		String document_Id = rs.getString("doc_Id");
		String isActive = rs.getString("doc_active");
		String activeString = Status.ACTIVE.name();
		boolean documentActive = false;
		if (isActive != null)
			documentActive = isActive.equalsIgnoreCase(activeString) == true ? true : false;
		if (document_Id != null && documentActive) {
			Document applicationDocument = new Document();
			applicationDocument.setId(document_Id);
			applicationDocument.setDocumentType(rs.getString("documenttype"));
			applicationDocument.setFileStoreId(rs.getString("filestoreid"));
			applicationDocument.setDocumentUid(rs.getString("doc_Id"));
			applicationDocument.setStatus(org.egov.waterConnection.model.Status.fromValue(isActive));
			waterConnection.addDocumentsItem(applicationDocument);
		}
		String plumber_id = rs.getString("plumber_id");
		if (plumber_id != null) {
			PlumberInfo plumber = new PlumberInfo();
			plumber.setId(plumber_id);
			plumber.setName(rs.getString("plumber_name"));
			plumber.setGender(rs.getString("plumber_gender"));
			plumber.setLicenseNo(rs.getString("licenseno"));
			plumber.setMobileNumber(rs.getString("plumber_mobileNumber"));
			plumber.setRelationship(rs.getString("relationship"));
			plumber.setCorrespondenceAddress(rs.getString("correspondenceaddress"));
			plumber.setFatherOrHusbandName(rs.getString("fatherorhusbandname"));
			waterConnection.addPlumberInfoItem(plumber);
		}
	}
}