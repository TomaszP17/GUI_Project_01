import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class S27477Plucinski26c {
    public static void main(String[] args) {
        List<Call> calls = new ArrayList<>();

        try{
            generateFileWithData("dane.txt");
            readFromFile("dane.txt", calls);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        while (true) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            int wybor = scanner.nextInt();
            switch (wybor) {
                case 1 -> printMostSecondsOfCallsAsCaller(calls);
                case 2 -> printLeastSecondsOfCallsAsReceiver(calls);
                case 3 -> printMostReceivers(calls);
                case 4 -> printMostCallers(calls);
                case 5 -> mostCallers(calls);
                case 6 -> mostReceivers(calls);
                case 7 -> leastCallers(calls);
                case 8 -> leastReceivers(calls);
                case 9 -> fullInformation(calls);
                case 0 -> System.exit(0);
                default -> System.out.println("Zla opcja, sprobuj ponownie :)");
            }
        }
    }
    public static void generateFileWithData(String filename) throws FileNotFoundException {
        Random random = new Random();
        File file = new File(filename);
        PrintWriter printWriter = new PrintWriter(file);
        final int R = 200_000;
        final int K = 500;
        for(int i = 0; i < R; i++){
            int caller = random.nextInt(K)  + 1;
            int receiver = random.nextInt(K) + 1;
            while(caller == receiver){
                receiver = random.nextInt(K) + 1;
            }
            int durationCall = (int) (Math.abs(random.nextGaussian()) * 3600);

            printWriter.write(caller + " " + receiver + " " + durationCall + '\n');
        }
        printWriter.close();
    }
    public static void printMenu(){
        System.out.println("---------------------------------------------");
        System.out.println("MENU PROGRAMU");
        System.out.println("[1] - Lista N klientow, ktorzy wygadali najwiecej czasu jako dzwoniacy i ich czas");
        System.out.println("[2] - Lista N klientow, ktorzy wygadali najwiecej czasu jako odbierajacy i ich czas");
        System.out.println("[3] - Lista N klientow, ktorzy dzwonili do najwiekszej liczby innych klientow");
        System.out.println("[4] - Lista N klientow, ktorzy odebrali rozmowy od najwiekszej ilosci innych klientow");
        System.out.println("[5] - Lista N klientow, ktorzy dzwonili najczesciej");
        System.out.println("[6] - Lista N klientow, ktorzy oderbali najwieksza liczbe rozmow");
        System.out.println("[7] - Lista N klientow, ktorzy dzwonili najrzadziej");
        System.out.println("[8] - Lista N klientow, ktorzy odebrali najmniejsza liczbe rozmow");
        System.out.println("[9] - pelna informacja o kliencie nr k; ile razy dzwonił, ile razy do niego dzwoniono, za ile sekund ma zaplacic");
        System.out.println("[0] - Koniec programu");
        System.out.println("---------------------------------------------");
    }
    public static void readFromFile(String filename, List<Call> calls) throws FileNotFoundException{
        try{
            Scanner scanner1 = new Scanner(new File(filename));
            while(scanner1.hasNextLine()){
                String line = scanner1.nextLine();
                String[] parts = line.split(" ");
                int callerId = Integer.parseInt(parts[0]);
                int receriverId = Integer.parseInt(parts[1]);
                int duration = Integer.parseInt(parts[2]);

                if(callerId != receriverId){
                    Call call = new Call(callerId, receriverId, duration);
                    calls.add(call);
                }
            }
            scanner1.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void printMostSecondsOfCallsAsCaller(List<Call> calls){
        Map<Integer, Integer> callsMap = new HashMap<>();

        for(Call x : calls){
            int caller = x.getCaller();
            int duration = x.getDuration();

            if(callsMap.containsKey(caller)){
                int oldDuration = callsMap.get(caller);
                callsMap.put(caller, duration + oldDuration);
            }else{
                callsMap.put(caller, duration);
            }
        }
        List<Map.Entry<Integer, Integer>> sortedCalls = callsMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordow ktore chcesz zobaczyc: ");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++){
            Map.Entry<Integer, Integer> entry = sortedCalls.get(i);
            System.out.println("Dzwoniacy: " + entry.getKey() + " suma czasu: " + entry.getValue());
        }
    }
    public static void printLeastSecondsOfCallsAsReceiver(List<Call> calls){
        Map<Integer, Integer> callsMap = new HashMap<>();

        for(Call x : calls){
            int receiver = x.getReceiver();
            int duration = x.getDuration();

            if(callsMap.containsKey(receiver)){
                int oldDuration = callsMap.get(receiver);
                callsMap.put(receiver, duration + oldDuration);
            }else{
                callsMap.put(receiver, duration);
            }
        }
        List<Map.Entry<Integer, Integer>> sortedCalls = callsMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordow ktore chcesz zobaczyc: ");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++){
            Map.Entry<Integer, Integer> entry = sortedCalls.get(i);
            System.out.println("Dzwoniacy: " + entry.getKey() + " suma czasu: " + entry.getValue());
        }
    }
    public static void printMostCallers(List<Call> calls){
        Map<Integer, List<Integer>> callsMap = new HashMap<>();
        for(Call x : calls){
            int caller = x.getCaller();
            int receiver = x.getReceiver();

            callsMap.putIfAbsent(caller, new ArrayList<>());
            List<Integer> receivers = callsMap.get(caller);

            if(!receivers.contains(receiver))
                receivers.add(receiver);
        }
        Map<Integer, Integer> receiverMap = new HashMap<>();
        for (List<Integer> receivers : callsMap.values()) {
            for (int receiver : receivers) {
                if (receiverMap.containsKey(receiver)) {
                    receiverMap.put(receiver, receiverMap.get(receiver) + 1);
                } else {
                    receiverMap.put(receiver, 1);
                }
            }
        }
        List<Map.Entry<Integer, Integer>> sortedEntries = receiverMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordow: ");
        int n = scanner.nextInt();

        // wyświetlanie wyników
        System.out.println("Najwięcej dzwonili do różnych odbiorców:");
        for (int i = 0; i < n; i++) {
            Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue() + " różnych odbiorców");
        }
    }
    public static void printMostReceivers(List<Call> calls) {
        Map<Integer, List<Integer>> callsMap = new HashMap<>();
        for (Call x : calls) {
            int caller = x.getCaller();
            int receiver = x.getReceiver();

            callsMap.putIfAbsent(receiver, new ArrayList<>());
            List<Integer> callers = callsMap.get(receiver);

            if(!callers.contains(caller))
                callers.add(caller);
        }

        // tworzenie mapy dzwoniących i liczba połączeń od różnych klientów
        Map<Integer, Integer> callerMap = new HashMap<>();
        for (List<Integer> callers : callsMap.values()) {
            for (int caller : callers) {
                if (callerMap.containsKey(caller)) {
                    callerMap.put(caller, callerMap.get(caller) + 1);
                } else {
                    callerMap.put(caller, 1);
                }
            }
        }

        List<Map.Entry<Integer, Integer>> sortedEntries = callerMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordów: ");
        int n = scanner.nextInt();

        // wyświetlanie wyników
        System.out.println("Najwięcej odebrali rozmow od różnych klientów:");
        for (int i = 0; i < n; i++) {
            Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue() + " różnych klientów");
        }
    }
    public static void mostCallers(List<Call> calls){
        Map<Integer, Integer> callerCountMap = new HashMap<>();

        for(Call x : calls){
            int caller = x.getCaller();
            callerCountMap.put(caller, callerCountMap.getOrDefault(caller, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> sortedEntries = callerCountMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordów: ");
        int n = scanner.nextInt();

        System.out.println("Najczęściej dzwonili klienci:");
        for (int i = 0; i < n && i < sortedEntries.size(); i++) {
            Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue() + " połączeń");
        }
    }
    public static void mostReceivers(List<Call> calls){
        Map<Integer, Integer> receiverCountMap = new HashMap<>();

        for(Call x : calls){
            int receiver = x.getReceiver();
            receiverCountMap.put(receiver, receiverCountMap.getOrDefault(receiver, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> sortedEntries = receiverCountMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj N rekordow: ");
        int n = scanner.nextInt();
        System.out.println("Najwieksza liczbe rozmow odebranych maja: ");
        for(int i = 0; i < n; i++){
            Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue() + " polaczen");
        }
    }
    public static void leastCallers(List<Call> calls) {
        Map<Integer, Integer> callerCountMap = new HashMap<>();

        for (Call x : calls) {
            int caller = x.getCaller();
            callerCountMap.put(caller, callerCountMap.getOrDefault(caller, 0) + 1);
        }

            List<Map.Entry<Integer, Integer>> sortedEntries = callerCountMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                    .toList();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Podaj N rekordów: ");
            int n = scanner.nextInt();

            System.out.println("Najrzadziej dzwonili klienci:");
            for (int i = 0; i < n && i < sortedEntries.size(); i++) {
                Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
                System.out.println(entry.getKey() + ": " + entry.getValue() + " połączeń");
            }
    }
    public static void leastReceivers(List<Call> calls) {
        Map<Integer, Integer> receiverCountMap = new HashMap<>();

        for (Call x : calls) {
            int receiver = x.getReceiver();
            receiverCountMap.put(receiver, receiverCountMap.getOrDefault(receiver, 0) + 1);
        }

            List<Map.Entry<Integer, Integer>> sortedEntries = receiverCountMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                    .toList();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Podaj N rekordow: ");
            int n = scanner.nextInt();
            System.out.println("Najrzadziej odbierali klienci: ");
            for (int i = 0; i < n; i++) {
                Map.Entry<Integer, Integer> entry = sortedEntries.get(i);
                System.out.println(entry.getKey() + " odebral: " + entry.getValue() + " polaczen");
            }
        }
    public static void fullInformation(List<Call> calls){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj id klienta ktorego mam wyswietlic informacje: ");
        int id = scanner.nextInt();
        int sumOfCalls = 0;
        int sumOfReceives = 0;
        int sumOfDur = 0;
        int sumOfToPay = 0;
        for(Call x : calls){
            int caller = x.getCaller();
            int receiver = x.getReceiver();
            int duration = x.getDuration();
            if(caller == id){
                sumOfCalls += 1;
                sumOfDur += duration;
                sumOfToPay += duration;
            }
            if(receiver == id){
                sumOfReceives += 1;
                sumOfDur += duration;
            }
        }
        System.out.println("Pelna informacja o kliencie nr: " + id);
        System.out.println("Ilosc zadzwonien: " + sumOfCalls);
        System.out.println("Ilosc odebran: " + sumOfReceives);
        System.out.println("Laczny czas rozmow: " + sumOfDur);
        System.out.println("Nalezy zaplacic: " + sumOfToPay);
    }
}
class Call {
    private final int caller;
    private final int receiver;
    private final int duration;

    public Call(int caller, int receiver, int duration) {
        this.caller = caller;
        this.receiver = receiver;
        this.duration = duration;
    }

    public int getCaller() {
        return caller;
    }
    public int getReceiver() {
        return receiver;
    }
    public int getDuration() {
        return duration;
    }
}