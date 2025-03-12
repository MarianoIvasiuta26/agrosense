package com.unam.agrosense.repository;

import com.unam.agrosense.model.actuador.Actuador;
import com.unam.agrosense.model.datoSensor.DatoSensor;
import com.unam.agrosense.model.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatoSensorRepository extends JpaRepository<DatoSensor, Long> {
    @Modifying
    @Query("UPDATE DatoSensor d SET d.activo = false WHERE d.id = :id")
    void softDelete(Long id);


    Optional<DatoSensor> findByIdAndActivoTrue(Long id);
    List<DatoSensor> findAllByActivoTrue();


}
