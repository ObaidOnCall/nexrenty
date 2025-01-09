package ma.crm.carental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.crm.carental.entities.Assurance;

/**
 * AssuranceRepo
 */
public interface AssuranceRepo extends JpaRepository<Assurance , Long>{

    
}
