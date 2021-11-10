package eredua.bean;

import java.util.Date;

public class LoginBean {
	private String izena;
	private String pasahitza;
	private Date data;

	public LoginBean() {
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String egiaztatu() {
		if (izena.equals("pirata"))
			return "error";
		else
			return "ok";
	}
}
