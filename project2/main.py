# -*- coding: utf-8 -*-
"""
Created on Thu Jan 28 00:44:25 2021

@author: chakati
"""
import os
import glob
import cv2
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
import handshape_feature_extractor  
import frameextractor as fe
from sklearn.metrics.pairwise import cosine_similarity

hfe = handshape_feature_extractor.HandShapeFeatureExtractor.get_instance()

# =============================================================================
# Get the penultimate layer for trainig data
# =============================================================================
# your code goes here
# Extract the middle frame of each gesture video

mp4_path = 'traindata/'
image_path = 'traindata/frames/'

count = 0
train_list = []
for filename in glob.glob(os.path.join(mp4_path, '*.mp4')):
    #with open(os.path.join(os.getcwd(), filename), 'r') as f: # open in readonly mode
    fe.frameExtractor(filename, image_path, count)
    image = cv2.imread(image_path + "%#05d.png" % (count+1), cv2.IMREAD_GRAYSCALE)
    train_list.append(hfe.extract_feature(image))
   
    count += 1
    print(filename, count)
    
#print(train_list)

# =============================================================================
# Get the penultimate layer for test data
# =============================================================================
# your code goes here 
# Extract the middle frame of each gesture video

mp4_path = 'test/'
image_path = 'test/frames/'

count = 0
test_list = []
for filename in glob.glob(os.path.join(mp4_path, '*.mp4')):

    fe.frameExtractor(filename, image_path, count)
    image = cv2.imread(image_path + "%#05d.png" % (count+1), cv2.IMREAD_GRAYSCALE)   
    test_list.append(hfe.extract_feature(image))
   
    count += 1
    print(filename, count)
#print(test_list)

# =============================================================================
# Recognize the gesture (use cosine similarity for comparing the vectors)
# =============================================================================

i = 0             #test_data label 
acc = 0           #count for accuracy
label_list = []   #list for results.csv


for test_data in test_list:
    c = 0         #count for train_data label 
    cs_max = 0
    #print("train data", train_data[0])   
    for train_data in train_list:

        #print("test data", test_data[0])
        
        cs_current = cosine_similarity(train_data, test_data)[0][0]
        if cs_max < cs_current:
            cs_max = cs_current
            label = c
        #print(i, c, cs_current, cs_max)
        c = (c + 1) % 17
        #c += 1
    print(i, label, cs_max)   
    label_list.append(label)
    
    if i == label:
        acc = acc + 1
    i = (i + 1) % 17
    #i += 1
print("Accuracy % = ", round(acc/51*100, 2))
print(label_list)
np.savetxt("Results.csv", label_list, delimiter =",", fmt='%i')
