
(ns app.home.frontend.screen.events
    (:require [app.home.frontend.screen.prototypes :as screen.prototypes]
              [map.api                             :refer [dissoc-in]]
              [re-frame.api                        :as r]
              [vector.api                          :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-screen!
  ; @return (map)
  [db _]
  (dissoc-in db [:home :screen/screen-loaded?]))

(defn screen-loaded
  ; @return (map)
  [db _]
  (assoc-in db [:home :screen/screen-loaded?] true))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-menu-item!
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :group-name (metamorphic-content)(opt)
  ;    Default: :other
  ;   :horizontal-weight (integer)(opt)
  ;    Default: 0
  ;   :icon (keyword)
  ;   :icon-color (string)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :label (string)
  ;   :on-click (metamorphic-event)}
  [db [_ item-props]]
  (let [item-props (screen.prototypes/item-props-prototype item-props)]
       (update-in db [:home :screen/menu-items] vector/conj-item item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :home.screen/screen-loaded screen-loaded)

; @usage
;  [:home.screen/add-menu-item! {:label    "My item"
;                                :icon     :festival
;                                :on-click [:x.router/go-to! "/@app-home/my-item"]}]
(r/reg-event-db :home.screen/add-menu-item! add-menu-item!)
