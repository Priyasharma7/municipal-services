package org.egov.pgr.repository.rowmapper;

import org.egov.pgr.web.models.RequestSearchCriteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class PGRQueryBuilder {


    private static final String INNER_JOIN_STRING = " INNER JOIN ";
    private static final String LEFT_OUTER_JOIN_STRING = " LEFT OUTER JOIN ";

    private static final String QUERY_ALIAS =   "ser.id as ser_id,ads.id as ads_id,doc.id as doc_id," +
                                                "ser.tenantId as ser_tenantId,ads.tenantId as ads_tenantId,doc.tenantId as doc_tenantId," +
                                                "ser.additionaldetails as ser_additionaldetails,ads.additionaldetails as ads_additionaldetails," +
                                                "ser.createdby as ser_createdby,ser.createdtime as ser_createdtime," +
                                                "ser.lastmodifiedby as ser_lastmodifiedby,ser.lastmodifiedtime as ser_lastmodifiedtime," +
                                                "ads.createdby as ads_createdby,ads.createdtime as ads_createdtime," +
                                                "ads.lastmodifiedby as ads_lastmodifiedby,ads.lastmodifiedtime as ads_lastmodifiedtime," +
                                                "doc.createdby as doc_createdby,doc.createdtime as doc_createdtime," +
                                                "doc.lastmodifiedby as doc_lastmodifiedby,doc.lastmodifiedtime as doc_lastmodifiedtime";

    private static final String QUERY = "select ser.*,ads.*,doc.*," + QUERY_ALIAS+
                                        " from eg_pgr_service_v2 ser INNER JOIN eg_pgr_address_v2 ads" +
                                        " ON ads.parentId = ser.id LEFT OUTER JOIN " +
                                        "eg_pgr_document_v2 doc ON doc.parentId = ser.id WEHRE";



    public String getTLSearchQuery(RequestSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(QUERY);

        if (criteria.getTenantId() != null) {
            addClauseIfRequired(preparedStmtList, builder);
            builder.append(" ser.tenantid=? ");
            preparedStmtList.add(criteria.getTenantId());
        }

        if (criteria.getServiceCode() != null) {
            addClauseIfRequired(preparedStmtList, builder);
            builder.append(" ser.serviceCode=? ");
            preparedStmtList.add(criteria.getServiceCode());
        }

        Set<String> ids = criteria.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(preparedStmtList, builder);
            builder.append(" ser.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }

        Set<String> userIds = criteria.getUserIds();
        if (!CollectionUtils.isEmpty(userIds)) {
            addClauseIfRequired(preparedStmtList, builder);
            builder.append(" ser.accountId IN (").append(createQuery(userIds)).append(")");
            addToPreparedStatement(preparedStmtList, userIds);
        }

        return builder.toString();
    }


    private static void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
        if (values.isEmpty())
            queryString.append(" WHERE ");
        else {
            queryString.append(" AND");
        }
    }

    private String createQuery(Collection<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for( int i = 0; i< length; i++){
            builder.append(" LOWER(?)");
            if(i != length -1) builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, Collection<String> ids)
    {
        ids.forEach(id ->{ preparedStmtList.add(id);});
    }

}
