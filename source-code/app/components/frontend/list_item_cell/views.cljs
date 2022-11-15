
(ns app.components.frontend.list-item-cell.views
    (:require [app.components.frontend.list-item-cell.helpers    :as list-item-cell.helpers]
              [app.components.frontend.list-item-cell.prototypes :as list-item-cell.prototypes]
              [elements.api                                      :as elements]
              [mid-fruits.random                                 :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-cell-body
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:rows (maps in vector)}
  [cell-id {:keys [rows] :as cell-props}]
  (letfn [(f [rows row]
             (conj rows [elements/label row]))]
         (reduce f [:<>] rows)))

(defn- list-item-cell
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [cell-id {:keys [class disabled? indent style] :as cell-props}]
  [:div (list-item-cell.helpers/cell-attributes cell-id cell-props)
        [elements/blank cell-id
                        {:class     class
                         :disabled? disabled?
                         :indent    indent
                         :content   [list-item-cell-body cell-id cell-props]
                         :style     style}]])

(defn component
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :rows (maps in vector)
  ;    {:color (keyword or string)(opt)
  ;      Default: :default
  ;     :content (metamorphic-content)
  ;     :font-size (keyword)(opt)
  ;       Default: :s
  ;     :font-weight (keyword)(opt)
  ;      :bold, extra-bold, :normal
  ;      Default: :bold
  ;     :placeholder (metamorphic-content)(opt)}
  ;   :style (map)(opt)
  ;   :width (keyword or string)(opt)
  ;    :stretch, "...px", "...%"}
  ;
  ; @usage
  ;  [list-item-cell {...}]
  ;
  ; @usage
  ;  [list-item-cell :my-cell {...}]
  ;
  ; @usage
  ;  [list-item-cell :my-cell {:rows [{:content "Row #1"}]}]
  ([cell-props]
   [component (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   (let [cell-props (list-item-cell.prototypes/cell-props-prototype cell-props)]
        [list-item-cell cell-id cell-props])))
