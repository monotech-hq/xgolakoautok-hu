
(ns app.components.frontend.item-list-header.helpers
    (:require [css.api :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ _]
  {:style {:position "sticky" :top "48px" :height "36px"
           :background-color "var( --fill-color )" :opacity ".98"
           :border-top-left-radius "12px"}})

(defn header-cell-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:cells (vector)}
  ; @param (integer) cell-dex
  ; @param (map) cell
  ;  {:width (px)(opt)}
  [_ {:keys [cells]} cell-dex {:keys [width]}]
  ; BUG#8705
  ; A border-radius tulajdonságot csak így lehetett alkalmazni a táblázaton :(
  {:style {:width                      (css/px width)
           :border-top-left-radius     (if (= cell-dex 0) "12px")
           :border-bottom-left-radius  (if (= cell-dex 0) "12px")
           :border-top-right-radius    (if (= cell-dex (-> cells count dec)) "12px")
           :border-bottom-right-radius (if (= cell-dex (-> cells count dec)) "12px")}})
