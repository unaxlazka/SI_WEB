package eredua.bean;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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
		if (izena.length() != pasahitza.length()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Errorea: izenaren eta pasahitzaren luzera desberdinak dira."));
			return null;
		}
		if (izena.equals("pirata"))
			return "error";
		else
			return "ok";
	}
}
