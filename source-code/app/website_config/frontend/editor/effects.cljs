
(ns app.website-config.frontend.editor.effects
  (:require [app.website-config.frontend.editor.views :as editor.views]
            [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-config.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-config.editor
                                                {:default-view-id :seo}]
                [:website-config.editor/render-editor!]]})

(r/reg-event-fx :website-config.editor/render-editor!
  [:x.ui/render-surface! :website-config.editor/view
                         {:content #'editor.views/view}])
