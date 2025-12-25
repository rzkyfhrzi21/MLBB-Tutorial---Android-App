package com.example.mlbbtutorial.model;

public class SkillModel {

    // ===== RAW FIELDS dari JSON skills[] =====
    public String skillName;     // skill_name
    public String skillIcon;     // skill_icon (nama file .webp)
    public String type;          // type: passive / active / ultimate
    public String description;   // description

    // ===== sudah diolah jadi String siap tampil =====
    public String cooldown;      // cooldown array -> "40, 36, 32" atau "-"
    public String manacost;      // manacost array -> "120" atau "-"
    public String unique;        // skill_unique array -> "AoE, Slow, Siege"

    public SkillModel() {
        // default constructor (opsional, berguna kalau nanti pakai Gson/Firestore)
    }

    public SkillModel(String skillName,
                      String skillIcon,
                      String type,
                      String description,
                      String cooldown,
                      String manacost,
                      String unique) {
        this.skillName = skillName;
        this.skillIcon = skillIcon;
        this.type = type;
        this.description = description;
        this.cooldown = cooldown;
        this.manacost = manacost;
        this.unique = unique;
    }

    // ===== Getter (opsional) =====
    public String getSkillName() { return skillName; }
    public String getSkillIcon() { return skillIcon; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getCooldown() { return cooldown; }
    public String getManacost() { return manacost; }
    public String getUnique() { return unique; }

    @Override
    public String toString() {
        return "SkillModel{" +
                "skillName='" + skillName + '\'' +
                ", skillIcon='" + skillIcon + '\'' +
                ", type='" + type + '\'' +
                ", cooldown='" + cooldown + '\'' +
                ", manacost='" + manacost + '\'' +
                ", unique='" + unique + '\'' +
                '}';
    }
}
