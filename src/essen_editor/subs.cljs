(ns essen-editor.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::content
 (fn [db]
   (:file/content db)))

(re-frame/reg-sub
 ::functions
 (fn [db]
   (:file/functions db)))
