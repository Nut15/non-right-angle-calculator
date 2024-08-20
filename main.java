package org.example;
import java.util.*;

public class Main {

    static Double cosRuleSideC(Double C, Double a, Double b) {
        C = Math.toRadians(C);
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) - 2 * a * b * Math.cos(C));
    }

    static Double cosRuleAngleC(Double a, Double b, Double c) {
        return Math.toDegrees(Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b)));
    }

    static Double sinRuleSideC(Double B, Double C, Double b) {
        B = Math.toRadians(B);
        C = Math.toRadians(C);
        return (b / Math.sin(B)) * Math.sin(C);
    }

    static Double sinRuleAngleC(Double B, Double b, Double c) {
        B = Math.toRadians(B);
        return Math.toDegrees(Math.asin(c / (b / Math.sin(B))));
    }

    static double inputValue(Scanner scanner, String[] valueNames, int position) {
        System.out.print(valueNames[position]); //eg. side a:
        String newVal = scanner.nextLine();
        if (!newVal.trim().isEmpty())
            if (!newVal.trim().equals("0")) {
                return Double.parseDouble(newVal);
            } else {
                return -2;
            }
        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String continueProgram = "yes";
        while (continueProgram.equals("yes")) {

            String[] valueNames = {"side a: ", "side b: ", "side c: ", "angle A: ", "angle B: ", "angle C: "};

            boolean error = true;
            int canCalculateCounter = 0;

            System.out.println("Enter the known side lengths of the triangle, if unknown enter nothing:");
            double side1 = inputValue(scanner, valueNames, 0);
            double side2 = inputValue(scanner, valueNames, 1);
            double side3 = inputValue(scanner, valueNames, 2);
            System.out.println("Enter the known angle magnitudes of the triangle (in degrees), if unknown enter nothing:");
            double angle1 = inputValue(scanner, valueNames, 3);
            double angle2 = inputValue(scanner, valueNames, 4);
            double angle3 = inputValue(scanner, valueNames, 5);

            //seeing if there is 3 values and if any of them is 0 (impossible to calculate)
            double[] allValues = {side1, side2, side3, angle1, angle2, angle3};
            for (double i : allValues) {
                if (i > 0) {
                    canCalculateCounter++;
                } else if (i == -2) {
                    canCalculateCounter = -6;
                }
            }

            //plusing all sides: make sure there can't be 3 angles no sides (impossible to calculate)
            while (side1 + side2 + side3 > -3 && canCalculateCounter > 2 && canCalculateCounter < 6) {
                error = false; //can calculate!
                if (angle1 <= -1) { //angle1 is empty
                    if (angle2 <= -1) { //angle2 is empty
                        if (angle3 <= -1) { //only sides have value
                            angle1 = cosRuleAngleC(side3, side2, side1);
                        } else { //only angle 3 have value
                            if (side1 <= -1) { //angle3 + side2,3 (angle2, angle3, side2, side3)
                                angle2 = sinRuleAngleC(angle3, side3, side2);
                            } else if (side2 <= -1) { //angle3 + side1,3 (angle1, angle3, side1, side3)
                                angle1 = sinRuleAngleC(angle3, side3, side1);
                            } else if (side3 <= -1) { //angle3 + side1,2 (angle3, all sides)
                                side3 = cosRuleSideC(angle3, side1, side2);
                            } else { //angle3 + all sides (angle2, angle3, all sides)
                                angle2 = sinRuleAngleC(angle3, side3, side2);
                            }
                        }
                    } else { //angle 2 has value
                        if (angle3 <= -1) { //angle2
                            if (side1 <= -1) { //angle2 + side2,3 (angle2, angle3, side2, side3)
                                angle3 = sinRuleAngleC(angle2, side2, side3);
                            } else if (side2 <= -1) { //angle2 + side1,3 (angle2, allSides)
                                side2 = cosRuleSideC(angle2, side1, side3);
                            } else if (side3 <= -1) { //angle2 + side1,2 (angle1, angle2, side1, side2)
                                angle1 = sinRuleAngleC(angle2, side2, side1);
                            } else { //angle2, all sides (angle2, angle3, all sides)
                                angle3 = sinRuleAngleC(angle2, side2, side3);
                            }
                        } else { //angle 2 and angle 3 have value
                            if (side1 <= -1) { //angle2,3 and side2,3
                                if (side2 <= -1) { //angle2,3 + side3 (all angles, side2, side3)
                                    side2 = sinRuleSideC(angle3, angle2, side3);
                                } else if (side3 <= -1) { //angle2,3 + side2 (all angles, side2, side3)
                                    side3 = sinRuleSideC(angle2, angle3, side2);
                                }
                                angle1 = 180 - (angle2 + angle3);
                            } else if (side2 <= -1) { //angle2,3 and side1,3
                                if (!(side3 <= -1)) { //angle2,3 + side1,3 (all angles, all sides){
                                    side2 = sinRuleSideC(angle3, angle2, side3);
                                }
                                angle1 = 180 - (angle2 + angle3);
                            } else if (side3 <= -1) { //angle2,3 and side1,2 (all angles, all sides)
                                side3 = sinRuleSideC(angle2, angle3, side2);
                                angle1 = 180 - (angle2 + angle3);
                            } else { //angle2,3 and side1,2,3 (all angles, all sides)
                                angle1 = 180 - (angle2 + angle3);
                            }
                        }
                    }
                } else { //angle1 has value
                    if (angle2 <= -1) { //angle1
                        if (angle3 == -1) { //angle1 + all sides (angle1, angle2, all sides)
                            angle2 = sinRuleAngleC(angle1, side1, side2);
                        } else { //angle1,3
                            if (side1 <= -1) { //angle1,3 + side2,3 (all angles, (side1), side3)
                                if (!(side3 <= -1)) { //angle1,3 + side3 (all angles, side1, side3)
                                    side1 = sinRuleSideC(angle3, angle1, side3);
                                }
                                angle2 = 180 - (angle1 + angle3);
                            } else if (side2 <= -1) { //angle1,3 + side1,3 (all angles, side1, (side3))
                                if (side3 <= -1) { //angle1,3 + side1 (all angles, side1, side3)
                                    side3 = sinRuleSideC(angle1, angle3, side1);
                                }
                                angle2 = 180 - (angle1 + angle3);
                            } else if (side3 <= -1) { //angle1,3 + side1,2 (all angles, all sides)
                                side3 = sinRuleSideC(angle1, angle3, side1);
                                angle2 = 180 - (angle1 + angle3);
                            } else { //angle1,3 + all sides (all angles, all sides)
                                angle2 = 180 - (angle1 + angle3);
                            }
                        }
                    } else { //angle1,2
                        if (angle3 <= -1) { //angle1,2
                            if (side1 <= -1) { //angle1,2 + side2,3 (all angles, (side1), side2, (side3))
                                if (!(side2 <= -1)) { //angle1,2 + side2 (all angles, (side1), side2)
                                    side1 = sinRuleSideC(angle2, angle1, side2);
                                }
                                angle3 = 180 - (angle1 + angle2);
                            } else if (side2 <= -1) { //angle1,2 + side1,3 (all angles, side1, side2, (side3))
                                side2 = sinRuleSideC(angle1, angle2, side1);
                                angle3 = 180 - (angle1 + angle2);
                            } else { //angle1,2 + side1,2 (all angles, all sides)
                                angle3 = 180 - (angle1 + angle2);
                            }
                        } else { //all angles
                            if (side1 <= -1) { //all angles + side2,3
                                if (side2 <= -1) { //all angles + side3 (all angles, side1, side3)
                                    side1 = sinRuleSideC(angle3, angle1, side3);
                                } else { //all angles + side2 (all angles, side1, side2)
                                    side1 = sinRuleSideC(angle2, angle1, side2);
                                }
                            } else if (side2 <= -1) { //all angles + side1,3
                                if (side3 <= -1) { //all angles + side1 (all angles, all sides)
                                    side3 = sinRuleSideC(angle1, angle3, side1);
                                }
                                side2 = sinRuleSideC(angle1, angle2, side1);
                            } else if (side3 <= -1) { //all angles + side1,2 (all angles, all sides)
                                side3 = sinRuleSideC(angle1, angle3, side1);
                            } else { //all angles + all sides
                                break;
                            }
                        }
                    }
                }
                allValues = new double[]{side1, side2, side3, angle1, angle2, angle3};
                canCalculateCounter = 0;
                for (double i : allValues) {
                    if (i > 0)
                        canCalculateCounter++;
                }
            }

            //error message
            if (!error) {
                System.out.println("The triangle's sides and angles (degrees) are:");
                for (int i = 0; i < allValues.length; i++) {
                    System.out.printf("%s%.2f%n", valueNames[i], allValues[i]);
                }
            } else
                System.out.println("unable to calculate");

            System.out.print("Would you like to calculate dimensions of another triangle?: ");
            continueProgram = scanner.nextLine();
        }
    }
}
