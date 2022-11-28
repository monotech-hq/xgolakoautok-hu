
(ns app.website-pages.frontend.editor.effects
    (:require [app.website-pages.frontend.editor.views :as editor.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-pages.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-pages.editor
                                                {:default-view-id :data}]
                [:website-pages.editor/render-editor!]]})

(r/reg-event-fx :website-pages.editor/render-editor!
  [:x.ui/render-surface! :website-pages.editor/view
                         {:content #'editor.views/view}])
