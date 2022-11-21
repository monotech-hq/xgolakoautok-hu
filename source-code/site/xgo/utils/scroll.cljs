(ns site.xgo.utils.scroll
 (:require [re-frame.api :as r]))

(defn scroll-into 
  ([element-id]
   (scroll-into element-id (clj->js {:block  "start"
                                     :inline "center"})))
  ([element-id config]
   (let [element (.getElementById js/document element-id)]
     (.scrollIntoView element (clj->js config)))))
                     
(r/reg-fx :scroll/scroll-into scroll-into)