
(ns app.common.frontend.item-lister.views
    (:require [app.common.frontend.item-editor.views :as item-editor.views]
              [app.components.frontend.api           :as components]
              [css.api                               :as css]
              [dom.api                               :as dom]
              [elements.api                          :as elements]
              [keyword.api                           :as keyword]
              [logical.api                           :refer [nor]]
              [math.api                              :as math]
              [random.api                            :as random]
              [re-frame.api                          :as r]
              [x.components.api                      :as x.components]))

;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-label
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:content (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :stretch? (boolean)(opt)}
  ;
  ; @usage
  ;  [list-item-label {...}]
  ;
  ; @usage
  ;  [list-item-label :my-label {...}]
  ([label-props]
   [list-item-label (random/generate-keyword) label-props])

  ([_ {:keys [content placeholder stretch?]}]
   [:div (if stretch? {:style {:flex-grow 1}})
         [elements/label {:color       "#333"
                          :content     content
                          :indent      {:horizontal :xs :right :xs}
                          :placeholder placeholder}]]))

(defn list-item-detail
  ; @param (keyword)(opt) detail-id
  ; @param (map) detail-props
  ;  {:content (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [list-item-detail {...}]
  ;
  ; @usage
  ;  [list-item-detail :my-detail {...}]
  ([detail-props]
   [list-item-detail (random/generate-keyword) detail-props])

  ([_ {:keys [content placeholder width]}]
   [:div {:style {:width width}}
         [elements/label {:color       "#777"
                          :content     content
                          :font-size   :xs
                          :indent      {:horizontal :xs :right :xs}
                          :placeholder placeholder}]]))

(defn list-item-details
  ; @param (keyword)(opt) details-id
  ; @param (map) details-props
  ;  {:contents (metamorphic-contents in vector)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [list-item-details {...}]
  ;
  ; @usage
  ;  [list-item-details :my-details {...}]
  ([details-props]
   [list-item-details (random/generate-keyword) details-props])

  ([_ {:keys [contents width]}]
   [:div {:style {:width width}}
         (letfn [(f [contents content]
                    (conj contents [elements/label {:color     "#777"
                                                    :content   content
                                                    :font-size :xs
                                                    :indent    {:right :xs}}]))]
                (reduce f [:<>] contents))]))

(defn list-item-primary-cell
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :stretch? (boolean)(opt)
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [list-item-primary-cell {...}]
  ;
  ; @usage
  ;  [list-item-primary-cell :my-primary-cell {...}]
  ([cell-props]
   [list-item-primary-cell (random/generate-keyword) cell-props])

  ([_ {:keys [description label placeholder stretch? timestamp]}]
   [:div (if stretch? {:style {:flex-grow 1}})
         (if (or label placeholder) [elements/label {:content label :placeholder placeholder :indent {:right :xs} :style {:color "#333"}}])
         (if timestamp              [elements/label {:content timestamp   :font-size :xs     :indent {:right :xs} :style {:color "#888"}}])
         (if description            [elements/label {:content description :font-size :xs     :indent {:right :xs} :style {:color "#888"}}])]))

(defn list-item-structure
  ; @param (keyword)(opt) structure-id
  ; @param (map) structure-props
  ;  {:cells (components in vector)
  ;   :separator (keyword)(opt)
  ;    :bottom, :top, :both}
  ;
  ; @usage
  ;  [list-item-structure {...}]
  ;
  ; @usage
  ;  [list-item-structure :my-structure {...}]
  ;
  ; @usage
  ;  (defn my-cell [])
  ;  [list-item-structure :my-structure {:cells [[my-cell]]}]
  ([structure-props]
   [list-item-structure (random/generate-keyword) structure-props])

  ([_ {:keys [cells separator]}]
   (let [style (case separator :bottom {:align-items "center" :border-bottom "1px solid #f0f0f0" :display "flex"}
                               :top    {:align-items "center" :border-top    "1px solid #f0f0f0" :display "flex"}
                               :both   {:align-items "center" :border-bottom "1px solid #f0f0f0"
                                                              :border-top    "1px solid #f0f0f0" :display "flex"}
                                       {:align-items "center" :display "flex"})]
        (reduce conj [:div {:style style}] cells))))

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
  (let [viewport-small? @(r/subscribe [:x.environment/viewport-small?])
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
       [components/surface-description {:content (if (nor disabled? (empty? search-term)) description)
                                        :disabled?        disabled?
                                        :horizontal-align :left
                                        :indent           {:top :m :left :xs}}]))

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
                   [elements/label {:color       :default
                                    :content     label
                                    :font-size   :xs
                                    :indent      {:horizontal :xxs}
                                    :line-height :block}]
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
  (let [viewport-small? @(r/subscribe [:x.environment/viewport-small?])]
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
  [components/surface-button ::item-lister-create-item-button
                             {:background-color "#5a4aff"
                              :color            "white"
                              :disabled?        disabled?
                              :icon             :add
                              :label            :add!
                              :on-click         [:x.router/go-to! create-item-uri]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-download-info
  ; @param (keyword) lister-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister-download-info :my-lister {...}]
  [lister-id {:keys [disabled?]}]
  (let [all-item-count        @(r/subscribe [:item-lister/get-all-item-count        lister-id])
        downloaded-item-count @(r/subscribe [:item-lister/get-downloaded-item-count lister-id])
        download-info          {:content :npn-items-downloaded :replacements [downloaded-item-count all-item-count]}]
       [components/surface-description {:content   download-info
                                        :disabled? disabled?}]))
