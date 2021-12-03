package nagusia;

import eredua.HibernateUtil;
import eredu.domeinua.Erabiltzailea;
import eredu.domeinua.LoginGertaera;

import org.hibernate.Query;
import org.hibernate.Session;
import java.util.*;

public class GertaerakSortu {
	public GertaerakSortu() {
	}

	private void createAndStoreLoginGertaera(Long id, String deskribapena, Date data) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		LoginGertaera e = new LoginGertaera();
		e.setId(id);
		e.setDeskribapena(deskribapena);
		e.setData(data);
		session.persist(e);
		session.getTransaction().commit();
	}

	private void createAndStoreLoginGertaera(String deskribapena, Date data) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		LoginGertaera e = new LoginGertaera();
		e.setDeskribapena(deskribapena);
		e.setData(data);
		session.persist(e);
		session.getTransaction().commit();
	}

	private void createAndStoreLoginGertaera(String erabil, boolean login, Date data) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from Erabiltzailea where izena= :erabiltzailea");
		q.setParameter("erabiltzailea", erabil);
		List result = q.list();

		LoginGertaera e = new LoginGertaera();
		try {
			e.setErabiltzailea((Erabiltzailea) result.get(0));
		} catch (Exception ex) {
			System.out.println("Errorea: erabiltzailea ez da existitzen" + ex.toString());
		}
		e.setLogin(login);
		e.setData(data);
		session.persist(e);
		session.getTransaction().commit();
	}

	private void createAndStoreErabiltzailea(String izena, String pasahitza, String mota) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Erabiltzailea u = new Erabiltzailea();
		u.setIzena(izena);
		u.setPasahitza(pasahitza);
		u.setMota(mota);
		session.persist(u);
		session.getTransaction().commit();
	}

	private List gertaerakZerrendatu() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from LoginGertaera").list();
		session.getTransaction().commit();
		return result;
	}

	public void printObjMemDB(String azalpena, Erabiltzailea e) {
		System.out.print("\tMem:<" + e + "> DB:<" + GertaerakBerreskuratuJDBC.getErabiltzaileaJDBC(e) + "> =>");
		System.out.println(azalpena);
	}

	public static void main(String[] args) {
		GertaerakSortu e = new GertaerakSortu();
//		System.out.println("Gertaeren sorkuntza:");
//		//// e.createAndStoreLoginGertaera(1L,"Anek ondo egin du logina", new Date());
//		//// e.createAndStoreLoginGertaera(2L,"Nerea saiatu da login egiten", new
//		//// Date());
//		//// e.createAndStoreLoginGertaera(3L,"Kepak ondo egin du logina", new Date());
//		// e.createAndStoreLoginGertaera("Anek ondo egin du logina", new Date());
//		// e.createAndStoreLoginGertaera("Nerea saiatu da login egiten", new Date());
//		// e.createAndStoreLoginGertaera("Kepak ondo egin du logina", new Date());
//		//
//		// List gertaerak = e.gertaerakZerrendatu();
//		// for (int i = 0; i < gertaerak.size(); i++) {
//		// LoginGertaera ev = (LoginGertaera) gertaerak.get(i);
//		// System.out.println("Id: " + ev.getId() + " Deskribapena: "
//		// + ev.getDeskribapena() + " Data: " + ev.getData());
//		// }
//		// System.out.println("Gertaeren zerrenda:");
//		e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
//		e.createAndStoreLoginGertaera("Ane", true, new Date());
//		e.createAndStoreLoginGertaera("Ane", false, new Date());
//		System.out.println("Gertaeren zerrenda:");
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		List result = session.createQuery("from LoginGertaera").list();
//		for (int i = 0; i < result.size(); i++) {
//			LoginGertaera ev = (LoginGertaera) result.get(i);
//			System.out.println("Id: " + ev.getId() + " Deskribapena: " + ev.getDeskribapena() + " Data: " + ev.getData()
//					+ " Login: " + ev.isLogin());
//		}
//		session.getTransaction().commit();
		System.out.println("======================");
		System.out.println("Objektuen Bizi-Zikloa:");
		System.out.println("======================");
		Erabiltzailea u = new Erabiltzailea();
		u.setIzena("Nerea");
		u.setPasahitza("1234");
		u.setMota("irakaslea");
		System.out.println("new => TRANSIENT");
		e.printObjMemDB("Nerea bakarrik dago memorian eta ez DBan", u);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(u);
		System.out.println("save => PERSISTENT");
		e.printObjMemDB("Nerea oraindik ez dago DBan commit egin ez delako", u);
		u.setPasahitza("1235");
		e.printObjMemDB("u.setPasahitza(\"1235\") exekutatu da, baina ez commit-a. Beraz, Nerea ez dagoDBan", u);
		session.getTransaction().commit();
		System.out.println("close (commit) => DETACHED");
		e.printObjMemDB("Commit egin da, DBan ikusten dira aldaketak", u);
		u.setPasahitza("1236");
		e.printObjMemDB("u.setPasahitza(\"1236\") exekutatu da, baina objektua ez dago DBarekin konektatuta (detached)",
				u);
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(u); // save IPINITA => ERROR
			session.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println(
					"save => ERROREA: objektua 'detached' dagoenez, 'save' horrekin objektu berdina berriz ere sartzen saiatzen da, eta gako-errorea sortuko da");
			session.getTransaction().rollback();
		}
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		u = (Erabiltzailea) session.get(Erabiltzailea.class, u.getIzena());
		System.out.println("get => PERSISTENT");
		u.setPasahitza("1236");
		session.save(u); // edo merge(u), saveOrUpdate(u), persist(u),...
		session.getTransaction().commit();
		e.printObjMemDB("u.setPasahitza(\"1236\") exekutatu da, objektua DBarekin konektatuta egonda", u);
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		u.setPasahitza("1236");
		session.saveOrUpdate(u);
		System.out.println("saveOrUpdate => PERSISTENT");
		e.printObjMemDB("objektua iraunkorra da orain, baina pasahitz berria ikusteko, commit egin behar da", u);
		session.save(u);
		System.out.println(
				"save => PERSISTENT: objektu iraunkorra konektatuta dago datu-basearekin; ez da beste berri bat sortzen baizik eta eguneratzen da; beraz, ez da errorerik sortuko");
		session.getTransaction().commit();
		System.out.println("close (commit) => DETACHED");
		e.printObjMemDB("\tCommit egin da, pasahitz berria ikusten da DBan", u);
		u.setPasahitza("1237");
		e.printObjMemDB("u.setPasahitza(\"1237\") exekutatu da, baina objektua ez dago DBarekin konektatuta (detached)",
				u);
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		u = (Erabiltzailea) session.get(Erabiltzailea.class, u.getIzena());
		System.out.println("get => PERSISTENT");
		e.printObjMemDB("Erabiltzailea berriz ekarri da datu-basetik, beraz, pasahitzaren aldaketa galdu egin da", u);
		session.delete(u);
		System.out.println("delete => TRANSIENT");
		e.printObjMemDB("Commit egin arte, objektua ez da datu-basetik ezabatuko", u);
		session.getTransaction().commit();
		System.out.println("close (commit) => DETACHED");
		e.printObjMemDB("Commit egin da, datu-basean ez dago, baina memorian bai", u);
	}
}