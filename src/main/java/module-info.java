module cz.upce.bdats_semc_cizek {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.upce.bdats_semc_cizek to javafx.fxml;
    exports cz.upce.bdats_semc_cizek;
}
