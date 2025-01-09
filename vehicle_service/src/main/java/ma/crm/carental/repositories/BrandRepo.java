package ma.crm.carental.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.crm.carental.entities.Brand;

/**
 * BrandRepo
 */
public interface BrandRepo extends JpaRepository<Brand , Long>{

    @Query("SELECT b FROM Brand b WHERE b.id = :id AND b.tenantId = :tenantId")
    Optional<Brand> findByIdAndTenantId(@Param("id")Long id , @Param("tenantId")String tenantId) ;
}
