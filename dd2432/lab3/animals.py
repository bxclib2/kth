import numpy as np

antelop = 0
ape  = 1
bat  = 2
bear = 3
beetle = 4
butterfly  = 5
camel = 6
cat  = 7
crocodile  = 8
dog  = 9
dragonfly  = 10
duck = 11
elephant  = 12
frog = 13
giraffe = 14
grasshopper  = 15
horse = 16
housefly  = 17
hyena = 18
kangaroo  = 19
lion = 20
moskito = 21
ostrich = 22
pelican = 23
penguin = 24
pig  = 25
rabbit = 26
rat  = 27
seaturtle = 28
skunk = 29
spider = 30
walrus = 31

snames = ['antelop',
   'ape',
   'bat',
   'bear',
   'beetle',
   'butterfly',
   'camel',
   'cat',
   'crocodile',
   'dog',
   'dragonfly',
   'duck',
   'elephant',
   'frog',
   'giraffe',
   'grasshopper',
   'horse',
   'housefly',
   'hyena',
   'kangaroo',
   'lion',
   'moskito',
   'ostrich',
   'pelican',
   'penguin',
   'pig',
   'rabbit',
   'rat',
   'seaturtle',
   'skunk',
   'spider',
   'walrus']

antlered = 0
articulations = 1
barks = 2
big = 3
bigears = 4
biting = 5
black = 6
blood = 7
brown = 8
climbing = 9
cns = 10
coldblooded = 11
curltail = 12
digging = 13
eatsanimals = 14
eatsanything = 15
eatscarrion = 16
eatsfish = 17
eatsflies = 18
eatsgarbage = 19
eatsgrass = 20
eggs = 21
eightlegged = 22
exoskeleton = 23
extremelysmall = 24
fatbody = 25
feathers = 26
feelerless = 27
feelers = 28
flying = 29
fourlegged = 30
fourwinged = 31
fur = 32
gibbous = 33
gnawteeth = 34
green = 35
grey = 36
hoofs = 37
humanlike = 38
jumping = 39
landliving = 40
lightbrown = 41
lissom = 42
livingoffspring = 43
longneck = 44
longnosed = 45
medium = 46
moving = 47
nervoussystem = 48
nib = 49
notflying = 50
oddtoed = 51
oxygenconsuming = 52
pairtoed = 53
pink = 54
pipetrakeas = 55
plates = 56
pouch = 57
proboscis = 58
robust = 59
ruminanting = 60
running = 61
shell = 62
shortnosed = 63
shorttail = 64
sixlegged = 65
small = 66
smellsterrible = 67
spine = 68
tail = 69
thinbody = 70
trakeas = 71
tusked = 72
twolegged = 73
twowinged = 74
warmblooded = 75
waterliving = 76
verybig = 77
verylongears = 78
verysmall = 79
white = 80
wingless = 81
wings = 82
yellow = 83


props = np.zeros((32, 84));

props[bat, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, landliving, fourlegged, flying, eatsflies, verysmall, grey, tail]] = 1;

props[rat, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, gnawteeth, tail, landliving, fourlegged, eatsgarbage, small, brown]] = 1

props[rabbit, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, landliving, fourlegged, jumping, shorttail, eatsgrass, gnawteeth, verylongears, small, white]] = 1

props[elephant, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, landliving, eatsgrass, robust, bigears, proboscis, tusked, tail, verybig, grey]] = 1

props[horse, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, hoofs, eatsgrass, landliving, oddtoed, running, big, brown, tail]] = 1

props[antelop, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, hoofs, shorttail, eatsgrass, pairtoed, landliving, ruminanting, lissom, antlered, running, medium, lightbrown]] = 1

props[giraffe, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, hoofs, eatsgrass, pairtoed, landliving, ruminanting, longneck, tail, big, yellow]] = 1

props[camel, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, hoofs, eatsgrass, pairtoed, landliving, gibbous, tail, big, yellow]] = 1

props[pig, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, hoofs, eatsgrass, pairtoed, landliving, digging, tail, curltail, big, pink]] = 1

props[walrus, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, eatsanimals, waterliving, tusked, verybig, brown]] = 1

props[skunk, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, landliving, eatscarrion, tail, smellsterrible, medium, black]] = 1

props[hyena, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, landliving, longnosed, shorttail, eatscarrion, medium, brown]] = 1

props[dog, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, eatsanimals, landliving, longnosed, tail, lissom, medium, brown, barks]] = 1

props[bear, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, shorttail, eatsanimals, landliving, longnosed, robust, big, brown]] = 1

props[lion, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, eatsanimals, landliving, shortnosed, tail, lissom, climbing, big, yellow]] = 1

props[cat, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, fourlegged, eatsanimals, landliving, shortnosed, tail, lissom, climbing, small, black]] = 1

props[ape, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, livingoffspring, landliving, shorttail, eatsanything, twolegged, shortnosed, humanlike, big, black]] = 1

props[kangaroo, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, fur, landliving, fourlegged, livingoffspring, pouch, jumping, eatsgrass, medium, tail, lightbrown]] = 1

props[duck, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, wings, nib, twolegged, feathers, eggs, flying, eatsgrass, small, white]] = 1

props[pelican, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, wings, nib, twolegged, feathers, eggs, flying, eatsfish, medium, white]] = 1

props[penguin, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, wings, nib, twolegged, feathers, eggs, notflying, eatsfish, waterliving, small, black]] = 1

props[ostrich, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, warmblooded, wings, nib, twolegged, feathers, eggs, notflying, eatsgrass, running, big, black]] = 1

props[crocodile, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, coldblooded, eggs, tail, waterliving, fourlegged, plates, eatsanimals, brown, big, tail]] = 1

props[seaturtle, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, coldblooded, eggs, tail, shell, fourlegged, eatsgrass, waterliving, big, brown]] = 1

props[frog, [oxygenconsuming, moving, nervoussystem, spine, cns, blood, coldblooded, waterliving, fourlegged, eggs, verysmall, jumping, eatsflies, green]] = 1

props[housefly, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, flying, proboscis, twowinged, fatbody, black]] = 1

props[moskito, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, flying, proboscis, twowinged, thinbody, lightbrown]] = 1

props[butterfly, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, flying, proboscis, fourwinged, fatbody, yellow]] = 1

props[beetle, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, flying, biting, fourwinged, fatbody, black, shell]] = 1

props[dragonfly, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, flying, biting, fourwinged, thinbody, brown]] = 1

props[grasshopper, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, trakeas, feelers, sixlegged, wings, fourwinged, flying, biting, jumping, fatbody, green]] = 1

props[spider, [oxygenconsuming, moving, nervoussystem, exoskeleton, articulations, eggs, extremelysmall, wingless, pipetrakeas, feelerless, eightlegged, fatbody, black]] = 1
