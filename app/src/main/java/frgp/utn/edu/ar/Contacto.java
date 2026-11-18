package frgp.utn.edu.ar;

public class Contacto {
    private int id;
    private String nombre;
    private String apellido;
    private String tipoTelefono;
    private String telefono;
    private String tipoEmail;
    private String email;
    private String direccion;
    private String fechaNacimiento;
    private String nivelEstudios;
    private String intereses;
    private boolean recibirInfo;

    public Contacto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTipoTelefono() { return tipoTelefono; }
    public void setTipoTelefono(String tipoTelefono) { this.tipoTelefono = tipoTelefono; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoEmail() { return tipoEmail; }
    public void setTipoEmail(String tipoEmail) { this.tipoEmail = tipoEmail; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNivelEstudios() { return nivelEstudios; }
    public void setNivelEstudios(String nivelEstudios) { this.nivelEstudios = nivelEstudios; }

    public String getIntereses() { return intereses; }
    public void setIntereses(String intereses) { this.intereses = intereses; }

    public boolean isRecibirInfo() { return recibirInfo; }
    public void setRecibirInfo(boolean recibirInfo) { this.recibirInfo = recibirInfo; }


    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + email;
    }
}
