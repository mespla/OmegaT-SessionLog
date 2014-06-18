import sys
from xml.dom import minidom

xmldoc = minidom.parse(sys.argv[1])
itemlist = xmldoc.getElementsByTagName('segment') 
sum=0.0
for s in itemlist :
  sum += float(s.attributes['duration'].value[:-1])
if sum >= 60:
  if sum >= 3600:
    h=sum//3600
    m=(sum % 3600)//60
    s=((sum % 3600) % 60)
    print "total="+str(h)+"h"+str(m)+"m"+str(s)+"s"
  else:
    m=sum//60
    s=sum % 60
    print "total="+str(m)+"m"+str(s)+"s"
else:
    print "total="+str(sum)+"s"
print "average="+str("{0:.2f}".format(round(sum/len(itemlist),2)))+"s"
