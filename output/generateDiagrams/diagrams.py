import csv
import pylab
from collections import defaultdict

outputFolder = 'C:\workspace\cloudsim\output'
metricsFolder = outputFolder + '\metrics'
powerFolder = outputFolder + '\power'
picturesFolder = outputFolder + '\pictures'
hostsFolder = outputFolder + '\hosts'
slaFolder = outputFolder + '\sla'
workloadPonaszki = 'ponaszki_'
overloadThreshold = 'thr_'
migrationPolicy = 'mc_'
parameter = '0.8_'
prefix_rr = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'rr_'+parameter
prefix_pck = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'pck_'+parameter
prefix_stp = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'stp_'+parameter
prefix_wpc = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'wpc_'+parameter
prefix_cpc = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'cpc_'+parameter
prefix_lbCpuRatio = '\\' + workloadPonaszki + overloadThreshold + migrationPolicy+'lbCpuRatio_'+parameter

#rr
avgUtilisationSuffixInputPath_rr = metricsFolder + prefix_rr + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_rr = metricsFolder + prefix_rr + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_rr = metricsFolder + prefix_rr + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_rr = metricsFolder + prefix_rr + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_rr = powerFolder + prefix_rr + 'power_in_time.csv'
powerInTimeCumInputPath_rr = powerFolder + prefix_rr + 'power_in_time_cum.csv'

#pck
avgUtilisationSuffixInputPath_pck = metricsFolder + prefix_pck + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_pck = metricsFolder + prefix_pck + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_pck = metricsFolder + prefix_pck + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_pck = metricsFolder + prefix_pck + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_pck = powerFolder + prefix_pck + 'power_in_time.csv'
powerInTimeCumInputPath_pck = powerFolder + prefix_pck + 'power_in_time_cum.csv'

#stp
avgUtilisationSuffixInputPath_stp = metricsFolder + prefix_stp + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_stp = metricsFolder + prefix_stp + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_stp = metricsFolder + prefix_stp + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_stp = metricsFolder + prefix_stp + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_stp = powerFolder + prefix_stp + 'power_in_time.csv'
powerInTimeCumInputPath_stp = powerFolder + prefix_stp + 'power_in_time_cum.csv'

#wpc
avgUtilisationSuffixInputPath_wpc = metricsFolder + prefix_wpc + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_wpc = metricsFolder + prefix_wpc + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_wpc = metricsFolder + prefix_wpc + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_wpc = metricsFolder + prefix_wpc + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_wpc = powerFolder + prefix_wpc + 'power_in_time.csv'
powerInTimeCumInputPath_wpc = powerFolder + prefix_wpc + 'power_in_time_cum.csv'

#cpc
avgUtilisationSuffixInputPath_cpc = metricsFolder + prefix_cpc + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_cpc = metricsFolder + prefix_cpc + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_cpc = metricsFolder + prefix_cpc + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_cpc = metricsFolder + prefix_cpc + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_cpc = powerFolder + prefix_cpc + 'power_in_time.csv'
powerInTimeCumInputPath_cpc = powerFolder + prefix_cpc + 'power_in_time_cum.csv'

#lbuCpuRatio
avgUtilisationSuffixInputPath_lbCpuRatio = metricsFolder + prefix_lbCpuRatio + 'avg_utilisation_time_metric.csv'
numberOfActiveHostsInputPath_lbCpuRatio = metricsFolder + prefix_lbCpuRatio + 'number_of_active_hosts.csv'
numverOfVmMigrationsInputPath_lbCpuRatio = metricsFolder + prefix_lbCpuRatio + 'num_of_vm_migrations.csv'
numverOfVmMigrationsCumInputPath_lbCpuRatio = metricsFolder + prefix_lbCpuRatio + 'num_of_vm_migrations_cum.csv'
powerInTimeInputPath_lbCpuRatio = powerFolder + prefix_lbCpuRatio + 'power_in_time.csv'
powerInTimeCumInputPath_lbCpuRatio = powerFolder + prefix_lbCpuRatio + 'power_in_time_cum.csv'

def readData(inputPath,):
    columns = defaultdict(list)
    with open(inputPath) as f:
        reader = csv.DictReader(f, delimiter = ';')  # read rows into a dictionary format
        for row in reader:  # read a row as {column1: value1, column2: value2,...}
            for (k, v) in row.items():# go over each column name and value
                columns[k].append(float(v.replace(',','.')))  # append the value into the appropriate list
                # based on column name k

    # print(columns[xname])
    # print(columns[yname])
    #
    # x = columns[xname][1:]
    # y = columns[yname][1:]
    return columns




#RR
#average utilisation in time
columns_rr_au = readData(avgUtilisationSuffixInputPath_rr)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_rr_au['time'], columns_rr_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_rr.png')
pylab.close()

#number of active hosts
columns_rr_ah = readData(numberOfActiveHostsInputPath_rr)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_rr_ah['time'], columns_rr_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_rr.png')
pylab.close()

#number of vm migrations
columns_rr_vm = readData(numverOfVmMigrationsInputPath_rr)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_rr_vm['migration_time'], columns_rr_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_rr.png')
pylab.close()

#number of vm migrations cumulative
columns_rr_vmc = readData(numverOfVmMigrationsCumInputPath_rr)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_rr_vmc['migration_time'], columns_rr_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_rr.png')
pylab.close()

#power in time
columns_rr_p = readData(powerInTimeInputPath_rr)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_rr_p['time'], columns_rr_p['power'])
pylab.savefig(picturesFolder + '\power_time_rr.png')
pylab.close()

#power in time cumulative
columns_rr_pc = readData(powerInTimeCumInputPath_rr)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm rr)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_rr_pc['time'], columns_rr_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_rr.png')
pylab.close()



#PCK
#average utilisation in time
columns_pck_au = readData(avgUtilisationSuffixInputPath_pck)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_pck_au['time'], columns_pck_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_pck.png')
pylab.close()

#number of active hosts
columns_pck_ah = readData(numberOfActiveHostsInputPath_pck)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_pck_ah['time'], columns_pck_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_pck.png')
pylab.close()

#number of vm migrations
columns_pck_vm = readData(numverOfVmMigrationsInputPath_pck)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_pck_vm['migration_time'], columns_pck_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_pck.png')
pylab.close()

#number of vm migrations cumulative
columns_pck_vmc = readData(numverOfVmMigrationsCumInputPath_pck)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_pck_vmc['migration_time'], columns_pck_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_pck.png')
pylab.close()

#power in time
columns_pck_p = readData(powerInTimeInputPath_pck)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_pck_p['time'], columns_pck_p['power'])
pylab.savefig(picturesFolder + '\power_time_pck.png')
pylab.close()

#power in time cumulative
columns_pck_pc = readData(powerInTimeCumInputPath_pck)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm pck)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_pck_pc['time'], columns_pck_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_pck.png')
pylab.close()



#STP
#average utilisation in time
columns_stp_au = readData(avgUtilisationSuffixInputPath_stp)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_stp_au['time'], columns_stp_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_stp.png')
pylab.close()

#number of active hosts
columns_stp_ah = readData(numberOfActiveHostsInputPath_stp)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_stp_ah['time'], columns_stp_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_stp.png')
pylab.close()

#number of vm migrations
columns_stp_vm = readData(numverOfVmMigrationsInputPath_stp)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_stp_vm['migration_time'], columns_stp_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_stp.png')
pylab.close()

#number of vm migrations cumulative
columns_stp_vmc = readData(numverOfVmMigrationsCumInputPath_stp)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_stp_vmc['migration_time'], columns_stp_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_stp.png')
pylab.close()

#power in time
columns_stp_p = readData(powerInTimeInputPath_stp)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_stp_p['time'], columns_stp_p['power'])
pylab.savefig(picturesFolder + '\power_time_stp.png')
pylab.close()

#power in time cumulative
columns_stp_pc = readData(powerInTimeCumInputPath_stp)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm stp)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_stp_pc['time'], columns_stp_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_stp.png')
pylab.close()



#WPC
#average utilisation in time
columns_wpc_au = readData(avgUtilisationSuffixInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_wpc_au['time'], columns_wpc_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_wpc.png')
pylab.close()

#number of active hosts
columns_wpc_ah = readData(numberOfActiveHostsInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_wpc_ah['time'], columns_wpc_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_wpc.png')
pylab.close()

#number of vm migrations
columns_wpc_vm = readData(numverOfVmMigrationsInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_wpc_vm['migration_time'], columns_wpc_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_wpc.png')
pylab.close()

#number of vm migrations cumulative
columns_wpc_vmc = readData(numverOfVmMigrationsCumInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_wpc_vmc['migration_time'], columns_wpc_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_wpc.png')
pylab.close()

#power in time
columns_wpc_p = readData(powerInTimeInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_wpc_p['time'], columns_wpc_p['power'])
pylab.savefig(picturesFolder + '\power_time_wpc.png')
pylab.close()

#power in time cumulative
columns_wpc_pc = readData(powerInTimeCumInputPath_wpc)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm wpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_wpc_pc['time'], columns_wpc_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_wpc.png')
pylab.close()



#CPC
#average utilisation in time
columns_cpc_au = readData(avgUtilisationSuffixInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_cpc_au['time'], columns_cpc_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_cpc.png')
pylab.close()

#number of active hosts
columns_cpc_ah = readData(numberOfActiveHostsInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_cpc_ah['time'], columns_cpc_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_cpc.png')
pylab.close()

#number of vm migrations
columns_cpc_vm = readData(numverOfVmMigrationsInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_cpc_vm['migration_time'], columns_cpc_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_cpc.png')
pylab.close()

#number of vm migrations cumulative
columns_cpc_vmc = readData(numverOfVmMigrationsCumInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_cpc_vmc['migration_time'], columns_cpc_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_cpc.png')
pylab.close()

#power in time
columns_cpc_p = readData(powerInTimeInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_cpc_p['time'], columns_cpc_p['power'])
pylab.savefig(picturesFolder + '\power_time_cpc.png')
pylab.close()

#power in time cumulative
columns_cpc_pc = readData(powerInTimeCumInputPath_cpc)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm cpc)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_cpc_pc['time'], columns_cpc_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_cpc.png')
pylab.close()



#lbCpuRatio
#average utilisation in time
columns_lbCpuRatio_au = readData(avgUtilisationSuffixInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależności średniej utylizacji hostów w zależności od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja hosta w danym momencie')
pylab.plot(columns_lbCpuRatio_au['time'], columns_lbCpuRatio_au['avg_utilisation'])
pylab.savefig(picturesFolder + '\\avg_utilisation_lbCpuRatio.png')
pylab.close()

#number of active hosts
columns_lbCpuRatio_ah = readData(numberOfActiveHostsInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależności średniej liczby aktywnych hostów w zależności od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('ilość aktywnych hostów')
pylab.plot(columns_lbCpuRatio_ah['time'], columns_lbCpuRatio_ah['num_of_active_hosts'])
pylab.savefig(picturesFolder + '\\activeHosts_lbCpuRatio.png')
pylab.close()

#number of vm migrations
columns_lbCpuRatio_vm = readData(numverOfVmMigrationsInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależności liczby migracji w zależności od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji w poprzednim  przedziale czasowym (5 minunut)')
pylab.plot(columns_lbCpuRatio_vm['migration_time'], columns_lbCpuRatio_vm['num_of_mig'])
pylab.savefig(picturesFolder + '\\vm_mig_lbCpuRatio.png')
pylab.close()

#number of vm migrations cumulative
columns_lbCpuRatio_vmc = readData(numverOfVmMigrationsCumInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależnościliczby migracji w zależności od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji od rospoczęcia symulacji')
pylab.plot(columns_lbCpuRatio_vmc['migration_time'], columns_lbCpuRatio_vmc['num_of_mig_cum'])
pylab.savefig(picturesFolder + '\\vm_mig_cum_lbCpuRatio.png')
pylab.close()

#power in time
columns_lbCpuRatio_p = readData(powerInTimeInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta w ciągu ostatniego interwału czasowego')
pylab.plot(columns_lbCpuRatio_p['time'], columns_lbCpuRatio_p['power'])
pylab.savefig(picturesFolder + '\power_time_lbCpuRatio.png')
pylab.close()

#power in time cumulative
columns_lbCpuRatio_pc = readData(powerInTimeCumInputPath_lbCpuRatio)
pylab.figure()
pylab.title('Wykres zależności zużytej mocy od czasu (algorytm lbCpuRatio)')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc zużyta od początku symulacji')
pylab.plot(columns_lbCpuRatio_pc['time'], columns_lbCpuRatio_pc['power_cum'])
pylab.savefig(picturesFolder + '\power_time_cum_lbCpuRatio.png')
pylab.close()


#avg_utilisation
lw = 0.3
pylab.figure()
pylab.title('Średnia utylizacja')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja')
pylab.plot(columns_rr_au['time'], columns_rr_au['avg_utilisation'], label='rr', linewidth=lw)
pylab.plot(columns_pck_au['time'], columns_pck_au['avg_utilisation'], label='pck', linewidth=lw)
pylab.plot(columns_stp_au['time'], columns_stp_au['avg_utilisation'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_au['time'], columns_cpc_au['avg_utilisation'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_au['time'], columns_wpc_au['avg_utilisation'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_au['time'], columns_lbCpuRatio_au['avg_utilisation'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\\avg_utilisation_all.png')
pylab.close()

# number of active hosts
lw = 0.3
pylab.figure()
pylab.title('liczba aktywnych hostów')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('średnia utylizacja')
pylab.plot(columns_rr_ah['time'], columns_rr_ah['num_of_active_hosts'], label='rr', linewidth=lw)
pylab.plot(columns_pck_ah['time'], columns_pck_ah['num_of_active_hosts'], label='pck', linewidth=lw)
pylab.plot(columns_stp_ah['time'], columns_stp_ah['num_of_active_hosts'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_ah['time'], columns_cpc_ah['num_of_active_hosts'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_ah['time'], columns_wpc_ah['num_of_active_hosts'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_ah['time'], columns_lbCpuRatio_ah['num_of_active_hosts'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\\num_of_active_hosts_all.png')
pylab.close()

# number of vm migrations
lw = 0.3
pylab.figure()
pylab.title('liczba migracji w czasie')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba kolejnych migracji w czasie')
pylab.plot(columns_rr_vm['migration_time'], columns_rr_vm['num_of_mig'], label='rr', linewidth=lw)
pylab.plot(columns_pck_vm['migration_time'], columns_pck_vm['num_of_mig'], label='pck', linewidth=lw)
pylab.plot(columns_stp_vm['migration_time'], columns_stp_vm['num_of_mig'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_vm['migration_time'], columns_cpc_vm['num_of_mig'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_vm['migration_time'], columns_wpc_vm['num_of_mig'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_vm['migration_time'], columns_lbCpuRatio_vm['num_of_mig'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\\num_of_vm_migrations_all.png')
pylab.close()

# number of vm migrations cumulative
lw = 0.3
pylab.figure()
pylab.title('liczba migracji w czasie')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('liczba migracji (szt)')
pylab.plot(columns_rr_vmc['migration_time'], columns_rr_vmc['num_of_mig_cum'], label='rr', linewidth=lw)
pylab.plot(columns_pck_vmc['migration_time'], columns_pck_vmc['num_of_mig_cum'], label='pck', linewidth=lw)
pylab.plot(columns_stp_vmc['migration_time'], columns_stp_vmc['num_of_mig_cum'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_vmc['migration_time'], columns_cpc_vmc['num_of_mig_cum'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_vmc['migration_time'], columns_wpc_vmc['num_of_mig_cum'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_vmc['migration_time'], columns_lbCpuRatio_vmc['num_of_mig_cum'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\\num_of_vm_migrations_cum_all.png')
pylab.close()

# power in time
lw = 0.3
pylab.figure()
pylab.title('Moc w czasie')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc (kWh)')
pylab.plot(columns_rr_p['time'], columns_rr_p['power'], label='rr', linewidth=lw)
pylab.plot(columns_pck_p['time'], columns_pck_p['power'], label='pck', linewidth=lw)
pylab.plot(columns_stp_p['time'], columns_stp_p['power'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_p['time'], columns_cpc_p['power'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_p['time'], columns_wpc_p['power'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_p['time'], columns_lbCpuRatio_p['power'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\power_in_time_all.png')
pylab.close()

# power in time cumulative
lw = 0.3
pylab.figure(figsize=(10,8), dpi=150)
pylab.title('Moc w czasie')
pylab.grid(True)
pylab.xlabel('czas(s)')
pylab.ylabel('moc (kWh)')
pylab.plot(columns_rr_pc['time'], columns_rr_pc['power_cum'], label='rr', linewidth=lw)
pylab.plot(columns_pck_pc['time'], columns_pck_pc['power_cum'], label='pck', linewidth=lw)
pylab.plot(columns_stp_pc['time'], columns_stp_pc['power_cum'], label='stp', linewidth=lw)
pylab.plot(columns_cpc_pc['time'], columns_cpc_pc['power_cum'], label='cpc', linewidth=lw)
pylab.plot(columns_wpc_pc['time'], columns_wpc_pc['power_cum'], label='wpc', linewidth=lw)
pylab.plot(columns_lbCpuRatio_pc['time'], columns_lbCpuRatio_pc['power_cum'], label='lbCR', linewidth=lw)
pylab.legend()
pylab.savefig(picturesFolder + '\\power_in_time_cum_all.png')
pylab.close()