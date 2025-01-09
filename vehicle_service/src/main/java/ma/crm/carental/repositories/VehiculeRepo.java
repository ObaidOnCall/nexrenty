package ma.crm.carental.repositories;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Vehicule;


public interface VehiculeRepo extends JpaRepository<Vehicule , Long>{
    
    Page<VehResponseDto> findByTenantId(String tenantId ,Pageable pageable) ;

    @Modifying
    @Query(value = "UPDATE vehicules SET matricule = COALESCE(:matricule, matricule),"+
                    "model_id = COALESCE(:model, model_id), "+
                    "price = COALESCE(:price, price) " +
                    "WHERE id IN :ids AND tenant_id =:tenant_id" , nativeQuery = true) 
    int updateVehiculesInBatch(
        @Param("ids") List<Long> ids ,
        @Param("tenant_id") String tenant ,
        @Param("matricule") String matricule ,
        @Param("model") Long model ,
        @Param("price") double price
        // @Param("metadata") String metadata 
    ) ;

    @Query(value = "SELECT v FROM Vehicule v WHERE v.id =:id AND v.tenantId =:tenant_id")
    Optional<Vehicule> findByIdAndTenantId(
        @Param("id") Long id ,
        @Param("tenant_id") String tenant
    ) ;


    @Modifying
    @Query(value = "DELETE FROM vehicules v WHERE v.id IN :ids AND v.tenant_id = :tenantId", nativeQuery = true)
    void deleteVehiculesByIdsAndTenantId(
        @Param("ids") List<Long> ids,
        @Param("tenantId") String tenantId
    );
}
