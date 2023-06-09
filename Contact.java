public class Contact {
    private String name;
        private String profilePicture;

        public Contact(String name, String profilePicture) {
            this.name = name;
            this.profilePicture = profilePicture;
        }

        public String getName() {
            return name;
        }

        public String getProfilePicture() {
            return getClass().getResource(profilePicture).toExternalForm();
        }
    }