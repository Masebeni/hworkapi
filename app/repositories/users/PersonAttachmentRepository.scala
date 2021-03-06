package repositories.users
import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import com.websudos.phantom.keys.PartitionKey
import com.websudos.phantom.reactivestreams._
import conf.connection.DataConnection
import domain.users.PersonAttachment

import scala.concurrent.Future
/**
  * Created by SONY on 2016-10-21.
  */
class PersonAttachmentRepository extends CassandraTable[PersonAttachmentRepository,PersonAttachment]{
  object organisationId extends StringColumn(this) with PartitionKey[String]
  object userId extends StringColumn(this) with PrimaryKey[String]
  object personAttachmentId extends StringColumn(this) with PrimaryKey[String]
  object url extends StringColumn(this)
  object description extends StringColumn(this)
  object mime extends StringColumn(this)
  object date extends DateTimeColumn(this)

  override def fromRow(r: Row): PersonAttachment = {
    PersonAttachment(organisationId(r), userId(r), personAttachmentId(r), url(r), description(r), mime(r), date(r))
  }
}

object PersonAttachmentRepository extends PersonAttachmentRepository with RootConnector {
  override lazy val tableName = "personattachment"

  override implicit def space: KeySpace = DataConnection.keySpace

  override implicit def session: Session = DataConnection.session

  def save(personAttachment: PersonAttachment): Future[ResultSet] = {
    insert
      .value(_.organisationId, personAttachment.organisationId)
      .value(_.userId, personAttachment.userId)
      .value(_.personAttachmentId, personAttachment.personAttachmentId)
      .value(_.url, personAttachment.url)
      .value(_.description, personAttachment.description)
      .value(_.mime, personAttachment.mime)
      .value(_.date, personAttachment.date)

      .future()
  }

  def findById(organisationId: String, userId: String, personAttachmentId: String):Future[Option[PersonAttachment]] = {
    select.where(_.organisationId eqs organisationId). and(_.userId eqs userId). and (_.personAttachmentId eqs personAttachmentId).one()
  }
  def findAll: Future[Seq[PersonAttachment]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }
  def getPersonAttachment(organisationId: String): Future[Seq[PersonAttachment]] = {
    select.where(_.organisationId eqs organisationId).fetchEnumerator() run Iteratee.collect()
  }

  def deleteById(organisationId:String, userId: String, personAttachmentId: String): Future[ResultSet] = {
    delete.where(_.organisationId eqs organisationId). and(_.userId eqs userId). and (_.personAttachmentId eqs personAttachmentId).future()
  }
}
