
(ns app.common.frontend.item-lister.views
    (:require [app.common.frontend.item-editor.views    :as item-editor.views]
              [app.common.frontend.surface-button.views :as surface-button.views]
              [dom.api                                  :as dom]
              [elements.api                             :as elements]
              [mid-fruits.candy                         :refer [param]]
              [mid-fruits.css                           :as css]
              [mid-fruits.keyword                       :as keyword]
              [mid-fruits.logical                       :refer [nor]]
              [math.api                          :as math]
              [re-frame.api                             :as r]
              [x.app-components.api                     :as x.components]))

;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-icon-button
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:disabled? (boolean)(opt)
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;   :on-click (metamorphic-event)}
  ;
  ; @usage
  ;  [list-item-icon-button :my-lister 0 {...}]
  [_ _ {:keys [disabled? icon icon-family on-click]}]
  [:button {:on-click #(do (dom/stop-propagation! %)
                           (if-not disabled? (r/dispatch on-click)))
            :data-disabled disabled?}
           (if icon-family [elements/icon {:icon icon :icon-family icon-family}]
                           [elements/icon {:icon icon :indent {:vertical :xxs}}])])

(defn list-item-thumbnail
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:thumbnail (string)}
  ;
  ; @usage
  ;  [list-item-thumbnail :my-lister 0 {...}]
  [_ _ {:keys [thumbnail]}]
  [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs :vertical :xs}
                       :uri thumbnail :width :l}])

(defn list-item-thumbnail-icon
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)(opt)}
  ;
  ; @usage
  ;  [list-item-thumbnail-icon :my-lister 0 {...}]
  [_ _ {:keys [icon icon-family]}]
  (if icon-family [elements/icon {:icon icon :icon-family icon-family :indent {:horizontal :m :vertical :xl}}]
                  [elements/icon {:icon icon                          :indent {:horizontal :m :vertical :xl}}]))

(defn list-item-label
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:content (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :stretch? (boolean)(opt)}
  ;
  ; @usage
  ;  [list-item-label :my-lister 0 {...}]
  [_ _ {:keys [content placeholder stretch?]}]
  [:div (if stretch? {:style {:flex-grow 1}})
        [elements/label {:color       "#333"
                         :content     content
                         :indent      {:horizontal :xs :right :xs}
                         :placeholder placeholder}]])

(defn list-item-detail
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:content (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [list-item-detail :my-lister 0 {...}]
  [_ _ {:keys [content placeholder width]}]
  [:div {:style {:width width}}
        [elements/label {:color       "#777"
                         :content     content
                         :font-size   :xs
                         :indent      {:horizontal :xs :right :xs}
                         :placeholder placeholder}]])

(defn list-item-details
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:contents (metamorphic-contents in vector)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [list-item-details :my-lister 0 {...}]
  [lister-id item-dex {:keys [contents width]}]
  [:div {:style {:width width}}
        (letfn [(f [contents content]
                   (conj contents [elements/label {:color     "#777"
                                                   :content   content
                                                   :font-size :xs
                                                   :indent    {:right :xs}}]))]
               (reduce f [:<>] contents))])

(defn list-item-primary-cell
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :stretch? (boolean)(opt)
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [list-item-primary-cell :my-lister 0 {...}]
  [_ _ {:keys [description label placeholder stretch? timestamp]}]
  [:div (if stretch? {:style {:flex-grow 1}})
        (if (or label placeholder) [elements/label {:content label :placeholder placeholder :indent {:right :xs} :style {:color "#333" :line-height "21px"}}])
        (if timestamp              [elements/label {:content timestamp   :font-size :xs     :indent {:right :xs} :style {:color "#888" :line-height "18px"}}])
        (if description            [elements/label {:content description :font-size :xs     :indent {:right :xs} :style {:color "#888" :line-height "18px"}}])])


(defn list-item-structure
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:cells (components in vector)}
  ;
  ; @usage
  ;  [list-item-structure :my-lister 0 {...}]
  ;
  ; @usage
  ;  (defn my-cell [])
  ;  [list-item-structure :my-lister 0 {:cells [[my-cell]]}]
  [lister-id item-dex {:keys [cells]}]
  (let [item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])]
       (reduce conj [:div {:style {:align-items "center" :border-bottom (if-not item-last? "1px solid #f0f0f0") :display "flex"}}]
                    (param cells))))

;; -- Search components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-search-field
  ; @param (keyword) lister-id
  ; @param (map) field-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :search-keys (keywords in vector)}
  ;
  ; @usage
  ;  [item-lister-search-field :my-lister {...}]
  [lister-id {:keys [disabled? placeholder search-keys]}]
  (let [viewport-small? @(r/subscribe [:environment/viewport-small?])
        search-event     [:item-lister/search-items! lister-id {:search-keys search-keys}]]
       [elements/search-field ::item-lister-search-field
                              {:autoclear?    true
                               :autofocus?    true
                               :disabled?     disabled?
                               :border-radius (if viewport-small? :none :l)
                               :indent        {:top :xs}
                               :on-empty      search-event
                               :on-type-ended search-event
                               :placeholder   placeholder}]))

(defn item-lister-search-description
  ; @param (keyword) lister-id
  ; @param (map) description-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister-search-description :my-lister {...}]
  [lister-id {:keys [disabled?]}]
  (let [search-term    @(r/subscribe [:item-lister/get-meta-item      lister-id :search-term])
        all-item-count @(r/subscribe [:item-lister/get-all-item-count lister-id])
        description     (x.components/content {:content :search-results-n :replacements [all-item-count]})]
       [elements/label ::item-lister-search-description
                       {:color     :highlight
                        :content   (if (nor disabled? (empty? search-term)) description)
                        :font-size :xxs
                        :indent    {:top :m :left :xs}}]))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-header-spacer
  ; @param (keyword) lister-id
  ; @param (map) spacer-props
  ;  {:width (string)}
  ;
  ; @usage
  ;  [item-lister-header-spacer :my-lister {...}]
  [_ {:keys [width]}]
  [:div {:style {:width width}}])

(defn item-lister-header-cell
  ; @param (keyword) lister-id
  ; @param (map) cell-props
  ;  {:label (metamorphic-content)
  ;   :order-by-key (namespaced keyword)(opt)
  ;   :stretch? (boolean)(opt)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [item-lister-header-cell :my-lister {...}]
  [lister-id {:keys [label order-by-key stretch? width]}]
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by lister-id])
        current-order-by-key       (keyword/get-namespace current-order-by)
        current-order-by-direction (keyword/get-name      current-order-by)]
       [:div {:style {:display "flex" :width width :flex-grow (if stretch? 1 0)}}
             (cond (nil? order-by-key)
                   [elements/label {:color     :default
                                    :content   label
                                    :font-size :xs
                                    :indent    {:horizontal :xxs}}]
                   (= order-by-key current-order-by-key)
                   [elements/button {:color            :default
                                     :icon             (case current-order-by-direction :descending :arrow_drop_down :ascending :arrow_drop_up)
                                     :on-click         [:item-lister/swap-items! lister-id]
                                     :font-size        :xs
                                     :font-weight      :extra-bold
                                     :horizontal-align :left
                                     :icon-position    :right
                                     :indent           {:horizontal :xxs}
                                     :label            label}]
                   :else
                   [elements/button {:color            :muted
                                     :icon             :arrow_drop_down
                                     :on-click         [:item-lister/order-items! lister-id (keyword/add-namespace order-by-key :descending)]
                                     :font-size        :xs
                                     :horizontal-align :left
                                     :icon-position    :right
                                     :indent           {:horizontal :xxs}
                                     :label            label}])]))

(defn item-lister-header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:cells (maps in vector)
  ;    [{:label (metamorphic-content)
  ;      :order-by-key (namespaced keyword)(opt)
  ;      :stretch? (boolean)(opt)
  ;      :width (string)(opt)}]
  ;   :control-bar (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-lister-header :my-lister {...}]
  [lister-id {:keys [cells control-bar]}]
  (if-let [data-received? @(r/subscribe [:item-lister/data-received? lister-id])]
          [:div {:style {:background-color "var( --fill-color )" :border-bottom "1px solid var( --border-color-highlight )"
                         :display "flex" :opacity ".98" :flex-direction "column"  :position "sticky" :top "48px"
                         :border-radius "var( --border-radius-m ) var( --border-radius-m ) 0 0"}}
                (if control-bar [x.components/content control-bar])
                (letfn [(f [wrapper cell] (conj wrapper cell))]
                       (reduce f [:div {:style {:display "flex" :width "100%"}}] cells))]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-wrapper
  ; @param (keyword) lister-id
  ; @param (map) wrapper-props
  ;  {:body (metamorphic-content)
  ;   :header (metamorphic-content)}
  ;
  ; @usage
  ;  [item-lister-wrapper :my-lister {...}]
  [lister-id {:keys [body header]}]
  (let [viewport-small? @(r/subscribe [:environment/viewport-small?])]
       [:div {:style {:display "flex" :flex-direction "column-reverse"
                      :background-color "var( --fill-color )" :border "1px solid var( --border-color-highlight )"
                      :border-radius (if viewport-small? "0" "var( --border-radius-m )")}}
             [:div {:style {:width "100%" :overflow "hidden" :border-radius "0 0 var( --border-radius-m ) var( --border-radius-m )"}}
                   [x.components/content body]]
             [x.components/content header]]))

;; -- Ghost-view components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-ghost-element
  ; @param (keyword) lister-id
  ; @param (map) element-props
  ;
  ; @usage
  ;  [item-lister-ghost-element :my-lister {...}]
  [_ {:keys []}]
  [:div {:style {:padding "12px 12px" :width "100%"}}
        [:div {:style {:display "flex" :flex-direction "column" :width "100%" :grid-row-gap "24px"}}
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :l :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :l :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :l :indent {}}]]]])

(defn item-lister-ghost-header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;
  ; @usage
  ;  [item-lister-ghost-header :my-lister {...}]
  [_ {:keys []}]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:padding-bottom "6px" :width "240px"}}
              [elements/ghost {:height :xl}]]
        [:div {:style {:display "flex" :grid-column-gap "12px" :padding-top "6px"}}
              [:div {:style {:width "80px"}} [elements/ghost {:height :xs}]]
              [:div {:style {:width "80px"}} [elements/ghost {:height :xs}]]]
        [:div {:style {:width "100%" :padding-top "12px" :padding-bottom "48px"}}
              [elements/ghost {:height :l}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-create-item-button
  ; @param (keyword) lister-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :create-item-uri (string)}
  ;
  ; @usage
  ;  [item-lister-create-item-button :my-lister {...}]
  [_ {:keys [disabled? create-item-uri]}]
  [surface-button.views/element ::item-lister-create-item-button
                                {:background-color "#5a4aff"
                                 :color            "white"
                                 :disabled?        disabled?
                                 :icon             :add
                                 :label            :add!
                                 :on-click         [:router/go-to! create-item-uri]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-download-info
  ; @param (keyword) lister-id
  ; @param (map) info-props
  ;
  ; @usage
  ;  [item-lister-download-info :my-lister {...}]
  [lister-id _]
  (let [all-item-count        @(r/subscribe [:item-lister/get-all-item-count        lister-id])
        downloaded-item-count @(r/subscribe [:item-lister/get-downloaded-item-count lister-id])
        content {:content :npn-items-downloaded :replacements [downloaded-item-count all-item-count]}]
       [elements/horizontal-polarity ::item-lister-download-info
                                     {:middle-content [elements/label ::item-lister-download-info-label
                                                                      {:color     :highlight
                                                                       :content   content
                                                                       :font-size :xxs
                                                                       :indent    {:horizontal :xxs}}]}]))
