package fh.server.repository;

import fh.server.entity.widget.WidgetComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("postElementRepository")
public interface WidgetComponentRepository extends JpaRepository<WidgetComponent, String> {

}
