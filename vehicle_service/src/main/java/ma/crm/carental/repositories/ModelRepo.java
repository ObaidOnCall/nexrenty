package ma.crm.carental.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.crm.carental.entities.Model;


/**
 * ModelRepo
 */
public interface ModelRepo extends JpaRepository<Model , Long>{
    
    @Modifying
    @Query(value = "UPDATE models SET " +
                    "name = COALESCE(:name, name), " +
                    "brand_id = COALESCE(:brand, brand_id), " +
                    "top_speed = COALESCE(:top_speed, top_speed), " +
                    "number_of_doors = COALESCE(:number_of_doors, number_of_doors) " +
                    "WHERE id = ANY(:ids) AND tenant_id = :tenant_id", nativeQuery = true)  
    int updateModelsInBatch(
        @Param("ids") Long[] ids ,
        @Param("tenant_id") String tenant ,
        @Param("brand") Long brand ,
        @Param("name") String name ,
        @Param("top_speed") int topSpeed ,
        @Param("number_of_doors") int numberOfDoors 
    ) ;


    //we will add aslo the low level Params updating batch .
}
