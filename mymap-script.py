import folium
import json
import numpy as np
from pprint import pprint
import randomcolor

m = folium.Map(location=[40.7128, -74.0060])

with open('results.json') as f:
    data = json.load(f)

rand_color = randomcolor.RandomColor(150)
mycolor = rand_color.generate(count=150)

count = 0    
for key in data:
    print key
    #print list(np.random.choice(range(256), size=3))
    count = count + 1
    print mycolor[count]
    offense = data[key]
    for key in offense:
        for location in offense[key]:
            latlon = location['code'].split(',')
            print latlon[0]
            print latlon[1]
            folium.Marker(
                location=[float(latlon[0]), float(latlon[1])],
                icon=folium.Icon(color='white',icon_color=mycolor[count]),
                popup=key
            ).add_to(m)            
    print '---------------------------\n'
    #break
        
m.save('crime-map.html')
