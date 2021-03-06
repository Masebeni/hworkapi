package services.Training

import domain.training.courses.CourseTargetGroups
import services.Training.Impl.CourseTargetGroupsServiceImpl

import scala.concurrent.Future
import com.websudos.phantom.dsl._
/**
 * Created by gavin.ackerman on 2016-11-09.
 */
trait CourseTargetGroupService {
  def createOrUpdate(course: CourseTargetGroups): Future[ResultSet]

  def getCourseTargetGroupById( id: String): Future[Option[CourseTargetGroups]]

  def getCourseTargetGroups(): Future[Seq[CourseTargetGroups]]

}

object CourseTargetGroupService{
  def apply: CourseTargetGroupService = new CourseTargetGroupsServiceImpl()
}
