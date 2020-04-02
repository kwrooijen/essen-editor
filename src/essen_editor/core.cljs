(ns essen-editor.core
  (:require
   [essen-editor.events.keyboard]
   [essen-editor.req :refer [fs parinfer]]
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [essen-editor.events :as events]
   [essen-editor.views :as views]
   [essen-editor.config :as config]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn wait-for [pred callback]
  (if (pred)
    (callback)
    (js/setTimeout #(wait-for pred callback)
                   10)))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
    (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (wait-for (fn [] (and fs parinfer)) (fn []))
  (dev-setup)
  (mount-root))
