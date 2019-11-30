package com.edi3.dao.impl.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.app_info.Constant;
import com.edi3.core.app_info.TimeModule;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.documents.Message;
import com.edi3.core.enumerations.ProcessResult;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.core.exсeption.ExecutorTaskNotFoundException;
import com.edi3.dao.common.CommonCollections;
import com.edi3.dao.i.business_processes.ExecutorTaskDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
public class ExecutorTaskDaoImpl implements ExecutorTaskDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(ExecutorTask executorTask) {
        sessionFactory.getCurrentSession().save(executorTask);
    }

    public void update(ExecutorTask executorTask) {
        sessionFactory.getCurrentSession().update(executorTask);
    }

    public void delete(ExecutorTask executorTask) {
        sessionFactory.getCurrentSession().delete(executorTask);
    }

    public ExecutorTask getById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        ExecutorTask executorTask = session.get(ExecutorTask.class, id);
        if (executorTask == null) throw new ExecutorTaskNotFoundException(id);
        return executorTask;
    }

    public List<ExecutorTask> getReviewTask(User executor, String filterString) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTask> cq = cb.createQuery(ExecutorTask.class);
        Root<ExecutorTask> e = cq.from(ExecutorTask.class);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = e.join("document", JoinType.INNER);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(e.get("executor"), executor),
                cb.equal(e.get("completed"), false),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                        e.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                cb.like(cb.lower(e.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("finalDate"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

        cq.select(e);
        TypedQuery<ExecutorTask> q = session.createQuery(cq);
        return q.getResultList();
    }

    public List<ExecutorTask> getControlledTask(User author, String filterString) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTask> cq = cb.createQuery(ExecutorTask.class);
        Root<ExecutorTask> e = cq.from(ExecutorTask.class);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = e.join("document", JoinType.INNER);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(e.get("author"), author),
                cb.equal(e.get("completed"), false),
                cb.notEqual(abstractDocumentJoin.type(), Message.class),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                        e.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                cb.like(cb.lower(e.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("finalDate"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

        cq.select(e);
        TypedQuery<ExecutorTask> q = session.createQuery(cq);
        return q.getResultList();
    }

    public List<ExecutorTask> getFilterByExecutorAndDocument(User executor, AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor and e.completed = false", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);

        return query.getResultList();
    }

    public List<ExecutorTask> getFilterByUserAndDocument(User user, AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and (e.author =:user or e.executor =:user)", ExecutorTask.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);

        return query.getResultList();
    }

    public List<ExecutorTask> getFilterByExecutorDocumentAndProcessType(User executor, AbstractDocumentEdi documentEdi, ProcessType processType) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor and e.processType =:processType and e.completed = false", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processType", processType);

        return query.getResultList();
    }

    public List<ExecutorTask> getWithdrawAvailable(User executor, AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor " +
                "and e.completed = true and e.result != :processResultCanceled " +
                "and e.draft = false and e.dateCompleted > :timeStamp", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processResultCanceled", ProcessResult.CANCELED);
        query.setParameter("timeStamp", TimeModule.addSecondsToCurrentTime(-Constant.MINUTES_WITHDRAW_AVAILABLE * 60L));
        return query.getResultList();
    }

    public HashMap<ExecutorTask, List<UploadedFile>> getSignaturesWithUploadedFiles(AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select new map(executor_task , uploaded_file) from ExecutorTask executor_task " +
                "left join UploadedFile AS uploaded_file " +
                "on executor_task.document.id = uploaded_file.document.id and  executor_task.id = uploaded_file.executorTask.id " +
                "where executor_task.document =:documentEdi and executor_task.completed = true and executor_task.result != :processResultCanceled " +
                "order by executor_task.dateCompleted, executor_task.date, executor_task.executor.id");  // Set correct type of Query ?

        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processResultCanceled", ProcessResult.CANCELED);

        @SuppressWarnings("unchecked") List<HashMap<String, Object>> list = query.list();

        // Type LinkedHashMap because we need sorting list after creating (HashMap doesn't let it)
        LinkedHashMap<ExecutorTask, List<UploadedFile>> mapSignaturesWithUploadedFiles
                = list.stream().filter(t -> Objects.nonNull(t.get("0"))).collect(LinkedHashMap::new,
                (LinkedHashMap<ExecutorTask, List<UploadedFile>> m, HashMap<String, Object> v) ->
                        m.put((ExecutorTask) v.get("0"),
                                (v.get("1") == null ?
                                        null :
                                        (m.get((ExecutorTask) v.get("0")) == null ?
                                                new LinkedList<UploadedFile>(Collections.singletonList((UploadedFile) v.get("1"))) :
                                                (LinkedList<UploadedFile>) CommonCollections.addElementAndReturnList(m.get(v.get("0")), (UploadedFile) v.get("1"))
                                        ))
                        ),
                LinkedHashMap::putAll);

        //noinspection ResultOfMethodCallIgnored
        mapSignaturesWithUploadedFiles.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().getDateCompleted())).map(Map.Entry::getKey).collect(Collectors.toList());

        return mapSignaturesWithUploadedFiles;
    }

    public ExecutorTask getDraft(User author, AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi " +
                "and e.draft = true and e.author =:author ", ExecutorTask.class);

        query.setParameter("author", author);  // Maybe it's exaggerate
        query.setParameter("documentEdi", documentEdi);

        return query.stream().findFirst().orElse(null);  // better than query.getSingleResult() cause can return null
    }
}
