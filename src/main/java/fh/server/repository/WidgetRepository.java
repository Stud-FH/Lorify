package fh.server.repository;

import fh.server.entity.widget.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("widgetRepository")
public interface WidgetRepository extends JpaRepository<Widget, String> {

}
