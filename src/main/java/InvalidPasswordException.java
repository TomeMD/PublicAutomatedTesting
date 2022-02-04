public class InvalidPasswordException extends Exception{

        private String passwd;

        public InvalidPasswordException(String passwd) {
            super("La contraseña introducida ("+ passwd +") es incorrecta.");
        }

        public String getPassword() {
            return passwd;
        }

        public void setPassword(String passwd) {
            this.passwd = passwd;
        }

}
