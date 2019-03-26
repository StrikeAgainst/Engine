package engine;

public enum Material {
    Wood, Metal, Concrete;

    public static double getFriction(Material m1, Material m2) {
        if (m1 == null || m2 == null)
            return 0;

        switch (m1) {
            case Wood:
                switch (m2) {
                    case Wood: return 0.35;
                    case Metal: return 0.25;
                    case Concrete: return 0.45;
                    default: return 0;
                }
            case Metal:
                switch (m2) {
                    case Wood: return 0.25;
                    case Metal: return 0.4;
                    case Concrete: return 0.4;
                    default: return 0;
                }
            case Concrete:
                switch (m2) {
                    case Wood: return 0.45;
                    case Metal: return 0.4;
                    case Concrete: return 0.5;
                    default: return 0;
                }
            default: return 0;
        }
    }

    public static double getRestitution(Material m1, Material m2) {
        if (m1 == null || m2 == null)
            return 0;

        switch (m1) {
            case Wood:
                switch (m2) {
                    case Wood: return 0.2;
                    case Metal: return 0.15;
                    case Concrete: return 0.1;
                    default: return 0;
                }
            case Metal:
                switch (m2) {
                    case Wood: return 0.15;
                    case Metal: return 0.1;
                    case Concrete: return 0.05;
                    default: return 0;
                }
            case Concrete:
                switch (m2) {
                    case Wood: return 0.1;
                    case Metal: return 0.05;
                    case Concrete: return 0;
                    default: return 0;
                }
            default: return 0;
        }
    }
}

