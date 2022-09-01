/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.managers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named(value = "instrumentManager")
public class InstrumentManager implements Serializable  {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    private String instruments;

    // Instruments
    private boolean electricGuitar;
    private boolean acousticGuitar;
    private boolean bass;
    private boolean drums;
    private boolean keyboard;
    private boolean vocals;
    private boolean other;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getInstruments() {
        return instruments;
    }

    public void setInstruments(String instruments) {
        this.instruments = instruments;
    }
    /*
    ==========================================================================
    Getter and Setter Methods for the PrimeFaces Select Boolean Checkbox Items
    ==========================================================================
     */

    public boolean isElectricGuitar() {
        return electricGuitar;
    }

    public void setElectricGuitar(boolean electricGuitar) {
        this.electricGuitar = electricGuitar;
    }

    public boolean isAcousticGuitar() {
        return acousticGuitar;
    }

    public void setAcousticGuitar(boolean acousticGuitar) {
        this.acousticGuitar = acousticGuitar;
    }

    public boolean isBass() {
        return bass;
    }

    public void setBass(boolean bass) {
        this.bass = bass;
    }

    public boolean isDrums() {
        return drums;
    }

    public void setDrums(boolean drums) {
        this.drums = drums;
    }

    public boolean isKeyboard() {
        return keyboard;
    }

    public void setKeyboard(boolean keyboard) {
        this.keyboard = keyboard;
    }

    public boolean isVocals() {
        return vocals;
    }

    public void setVocals(boolean vocals) {
        this.vocals = vocals;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    // Create and return a comma separated list of selected instrument names
    public String instrumentsList() {
        // Empty the instrument list first
        instruments = "";

        // Instruments
        if (acousticGuitar) {
            instruments += "Acoustic Guitar, ";
        }
        if (electricGuitar) {
            instruments += "Electric Guitar, ";
        }
        if (drums) {
            instruments += "Drums, ";
        }
        if (bass) {
            instruments += "Bass, ";
        }
        if (keyboard) {
            instruments += "Keyboard, ";
        }
        if (vocals) {
            instruments += "Vocals, ";
        }
        if (other) {
            instruments += "Other, ";
        }

        if(instruments.length() == 0) {
            return "No Instruments Listed";
        }

        // Do not include the last comma and space
        instruments = instruments.substring(0, instruments.length() - 2);

        return instruments;
    }
}
