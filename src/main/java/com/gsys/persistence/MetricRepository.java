package com.gsys.persistence;

import com.gsys.persistence.model.Metric;
import org.springframework.data.repository.CrudRepository;

public interface MetricRepository extends CrudRepository<Metric, Integer> {

}
