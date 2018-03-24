#!/usr/bin/ruby
users = ["dominik","pepa","kamil","martin","honza","tomas"]
sites = ["www.facebook.com","www.idnes.cz","www.youtube.com","www.cnn.com","www.ebay.com","www.ackee.cz","www.wikipedia.org","www.yahoo.com","www.cvut.cz", "www.twitter.com","www.live.com","www.apple.com"]
file = File.open("cassandra-10000.cql","w");
severity = ["OK","WARNING","DENIED"]

count = 10000
count.times do |i|
  
  date = Time.at(rand * Time.now.to_i).to_i
  user = users.sample
  site = sites.sample
  
  sevrand = rand(100)
  if sevrand < 100 and sevrand > 90
    sev = severity[2]
  elsif sevrand < 90 and sevrand > 70
    sev = severity[1]
  else
    sev = severity[0]
  end
  
  query = "INSERT INTO holoumar_solr_ks.logs(id, user,date,page,severity) VALUES (#{i}, '#{user}','#{date}','#{site}','#{sev}');";
  file.puts query   
end






