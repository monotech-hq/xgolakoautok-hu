
(ns app.home.frontend.screen.subs
    (:require [candy.api    :refer [return]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-menu-group-items
  ; @param (metamorphic-content) group-name
  ;
  ; @return (maps in vector)
  [db [_ group-name]]
  ; XXX#0092
  (let [menu-items (get-in db [:home :screen/menu-items] [])]
       (letfn [(f [group-items menu-item]
                  (if (=      group-name (:group-name menu-item))
                      (conj   group-items menu-item)
                      (return group-items)))]
              (reduce f [] menu-items))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :home.screen/get-menu-group-items get-menu-group-items)
