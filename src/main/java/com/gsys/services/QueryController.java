package com.gsys.services;

import com.gsys.persistence.SensorReadingRepository;
import com.gsys.services.json.MetricResult;
import com.gsys.services.json.QueryRequest;
import com.gsys.services.json.QueryResponse;
import com.gsys.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Autowired
    SensorReadingRepository sensorReadingRepository;

    /**
     * Gets the results based on the specific query
     * @param queryRequest query request. metricIds is required
     * @return Results of the query or error if request is not valid
     */
    @PostMapping("/get_data")
    public ResponseEntity<QueryResponse> registerSensor(@RequestBody QueryRequest queryRequest){

        //Validations
        if (queryRequest.getMetricIds() == null || queryRequest.getMetricIds().isEmpty()) {
            return  ResponseEntity.badRequest().body(new QueryResponse(MessageResponse.METRIC_REQUIRED));
        }

        boolean datesProvided = queryRequest.getStartDate() != null && queryRequest.getEndDate() != null;
        if (datesProvided){
            if ((ChronoUnit.DAYS.between(queryRequest.getStartDate(), queryRequest.getEndDate()) > 30 )) {
                return  ResponseEntity.badRequest().body(new QueryResponse(MessageResponse.DATE_OUT_RANGE));
            }
            if (queryRequest.getStartDate().isAfter(queryRequest.getEndDate())) {
                return  ResponseEntity.badRequest().body(new QueryResponse(MessageResponse.DATE_RANGE_INVALID));
            }
        }

        //Get Results
        final List<MetricResult> metricsResults = new ArrayList<>();
        getMetricResults(queryRequest, datesProvided, metricsResults);

        return  ResponseEntity.ok().body(new QueryResponse(MessageResponse.RESULTS_OK,metricsResults));
    }

    private void getMetricResults(QueryRequest queryRequest, boolean datesProvided, List<MetricResult> averageByMetricSensorAndDate) {
        if (datesProvided){
            if (noSensorIdsProvided(queryRequest)){
                averageByMetricSensorAndDate.addAll(getMetricResults( sensorReadingRepository.findAverageByMetricsAndDates(queryRequest.getMetricIds(), queryRequest.getStartDate(), queryRequest.getEndDate())));
            }else{
                averageByMetricSensorAndDate.addAll(getMetricResults( sensorReadingRepository.findAverageByMetricSensorAndDate(queryRequest.getMetricIds(), queryRequest.getSensorIds(), queryRequest.getStartDate(), queryRequest.getEndDate())));
            }
        }else{
            if (noSensorIdsProvided(queryRequest)){
                averageByMetricSensorAndDate.addAll(getMetricResults( sensorReadingRepository.findLatestValueByMetrics(queryRequest.getMetricIds())));
            }else{
                averageByMetricSensorAndDate.addAll(getMetricResults( sensorReadingRepository.findLatestValueByMetricsAndSensors(queryRequest.getMetricIds(), queryRequest.getSensorIds())));
            }
        }
    }

    private boolean noSensorIdsProvided(QueryRequest queryRequest) {
        return queryRequest.getSensorIds() == null || queryRequest.getSensorIds().isEmpty();
    }

    private List<MetricResult> getMetricResults(Collection<Object[]> averageByMetricSensorAndDate) {
        final List<MetricResult> results = new ArrayList<>(averageByMetricSensorAndDate.size());
        for (Object[] result : averageByMetricSensorAndDate) {
            final MetricResult metricResult = new MetricResult(result[0].toString(), Integer.parseInt(result[1].toString()), Double.parseDouble(result[2].toString()));
            results.add(metricResult);
        }
        return results;
    }


}
